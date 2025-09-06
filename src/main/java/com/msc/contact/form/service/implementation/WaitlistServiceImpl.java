package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.dto.WaitlistRequest;
import com.msc.contact.form.dto.WaitlistResponseDto;
import com.msc.contact.form.exception.DuplicateEmailException;
import com.msc.contact.form.exception.ResourceNotFoundException;
import com.msc.contact.form.model.Waitlist;
import com.msc.contact.form.model.OperationLog;
import com.msc.contact.form.repository.WaitlistRepository;
import com.msc.contact.form.service.WaitlistService;
import com.msc.contact.form.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final OperationLogService operationLogService;

    @Override
    @Transactional
    public ResponseDto joinWaitlist(WaitlistRequest dto) {
        // Check if email exists and is active
        Optional<Waitlist> activeWaitlist = waitlistRepository.findByEmailActive(dto.getEmail());
        if (activeWaitlist.isPresent()) {
            throw new DuplicateEmailException("Email already exists in waitlist");
        }
        
        // Check if email exists but is soft deleted
        Optional<Waitlist> existingWaitlist = waitlistRepository.findByEmailIncludingDeleted(dto.getEmail());
        
        if (existingWaitlist.isPresent() && existingWaitlist.get().getIsDeleted()) {
            // Restore the soft deleted record
            Waitlist waitlist = existingWaitlist.get();
            String currentUser = getCurrentUser();
            waitlistRepository.restoreById(waitlist.getId(), LocalDateTime.now(), currentUser);
            
            // Log the restoration operation
            operationLogService.logOperation(
                    "waitlist", 
                    waitlist.getId(), 
                    OperationLog.OperationType.UPDATE, 
                    "isDeleted: true", 
                    "isDeleted: false", 
                    "Waitlist entry restored from soft delete"
            );
            
            return ResponseDto.builder()
                    .id(waitlist.getId())
                    .message("Successfully rejoined the waitlist")
                    .build();
        }
        
        // Create new waitlist entry
        Waitlist waitlist = Waitlist.builder()
                .email(dto.getEmail())
                .build();

        Waitlist saved = waitlistRepository.save(waitlist);

        // Log the operation
        operationLogService.logOperation(
                "waitlist", 
                saved.getId(), 
                OperationLog.OperationType.CREATE, 
                null, 
                saved, 
                "New waitlist subscription"
        );

        return ResponseDto.builder()
                .id(saved.getId())
                .message("Successfully joined the waitlist")
                .build();
    }

    @Override
    public Page<WaitlistResponseDto> getWaitlists(Pageable pageable) {
        return waitlistRepository.findAllActive(pageable)
                .map(this::convertResponseDto);
    }

    @Override
    public Page<WaitlistResponseDto> getDeletedWaitlists(Pageable pageable) {
        return waitlistRepository.findAllDeleted(pageable)
                .map(this::convertResponseDto);
    }

    @Override
    @Transactional
    public void deleteWaitlist(Long id) {
        Waitlist waitlist = waitlistRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waitlist entry not found with id: " + id));
        
        // Store old data for logging
        Waitlist oldWaitlist = Waitlist.builder()
                .email(waitlist.getEmail())
                .build();
        // Set the ID manually since it's inherited from BaseEntity
        oldWaitlist.setId(waitlist.getId());
        
        // Perform soft delete
        String currentUser = getCurrentUser();
        waitlistRepository.softDeleteById(id, LocalDateTime.now(), currentUser);
        
        // Log the operation
        operationLogService.logOperation(
                "waitlist", 
                id, 
                OperationLog.OperationType.DELETE, 
                oldWaitlist, 
                null, 
                "Waitlist entry soft deleted"
        );
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return "system";
    }

    private WaitlistResponseDto convertResponseDto(Waitlist waitlist) {
        return WaitlistResponseDto.builder()
                .id(waitlist.getId())
                .email(waitlist.getEmail())
                .createdAt(waitlist.getCreatedAt())
                .build();
    }
}
