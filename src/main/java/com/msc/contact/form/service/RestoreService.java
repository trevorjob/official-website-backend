package com.msc.contact.form.service;

import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.exception.ResourceNotFoundException;
import com.msc.contact.form.model.OperationLog;
import com.msc.contact.form.repository.WaitlistRepository;
import com.msc.contact.form.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RestoreService {

    private final WaitlistRepository waitlistRepository;
    private final ContactRepository contactRepository;
    private final OperationLogService operationLogService;

    @Transactional
    public ResponseDto restoreWaitlist(Long id) {
        // Check if the waitlist exists and is deleted
        return waitlistRepository.findById(id)
                .filter(waitlist -> waitlist.getIsDeleted())
                .map(waitlist -> {
                    String currentUser = getCurrentUser();
                    waitlistRepository.restoreById(id, LocalDateTime.now(), currentUser);
                    
                    // Log the restoration
                    operationLogService.logOperation(
                            "waitlist",
                            id,
                            OperationLog.OperationType.UPDATE,
                            "isDeleted: true",
                            "isDeleted: false",
                            "Waitlist entry manually restored by admin"
                    );
                    
                    return ResponseDto.builder()
                            .id(id)
                            .message("Waitlist entry restored successfully")
                            .build();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Deleted waitlist entry not found with id: " + id));
    }

    @Transactional
    public ResponseDto restoreContact(Long id) {
        // Check if the contact exists and is deleted
        return contactRepository.findById(id)
                .filter(contact -> contact.getIsDeleted())
                .map(contact -> {
                    String currentUser = getCurrentUser();
                    // For contacts, we need to manually update since we don't have a restore method
                    contact.setIsDeleted(false);
                    contact.setModifiedTime(LocalDateTime.now());
                    contact.setModifiedBy(currentUser);
                    contactRepository.save(contact);
                    
                    // Log the restoration
                    operationLogService.logOperation(
                            "contacts",
                            id,
                            OperationLog.OperationType.UPDATE,
                            "isDeleted: true",
                            "isDeleted: false",
                            "Contact entry manually restored by admin"
                    );
                    
                    return ResponseDto.builder()
                            .id(id)
                            .message("Contact restored successfully")
                            .build();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Deleted contact not found with id: " + id));
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return "system";
    }
}
