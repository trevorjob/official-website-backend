package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.ContactRequest;
import com.msc.contact.form.dto.ContactResponseDto;
import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.exception.ResourceNotFoundException;
import com.msc.contact.form.model.Contact;
import com.msc.contact.form.model.OperationLog;
import com.msc.contact.form.repository.ContactRepository;
import com.msc.contact.form.service.ContactService;
import com.msc.contact.form.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final OperationLogService operationLogService;

    @Override
    @Transactional
    public ResponseDto submitContact(ContactRequest dto) {
        Contact contact = Contact.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .organisation(dto.getOrganisation())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .build();

        Contact saved = contactRepository.save(contact);

        // Log the operation
        operationLogService.logOperation(
                "contacts", 
                saved.getId(), 
                OperationLog.OperationType.CREATE, 
                null, 
                saved, 
                "New contact form submission"
        );

        return ResponseDto.builder()
                .id(saved.getId())
                .message("Contact form submitted successfully")
                .build();
    }

    @Override
    public ContactResponseDto getContactById(Long id) {
        Contact contact = contactRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        return convertToDto(contact);
    }

    @Override
    public Page<ContactResponseDto> getAllContacts(PageRequest pageRequest) {
        return contactRepository.findAllActive(pageRequest)
                .map(this::convertToDto);
    }

    @Override
    public Page<ContactResponseDto> getDeletedContacts(PageRequest pageRequest) {
        return contactRepository.findAllDeleted(pageRequest)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Contact contact = contactRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        
        if (!contact.getIsRead()) {
            Boolean oldReadStatus = contact.getIsRead();
            contact.setIsRead(true);
            contact = contactRepository.save(contact);
            
            // Log the operation
            operationLogService.logOperation(
                    "contacts", 
                    id, 
                    OperationLog.OperationType.UPDATE, 
                    oldReadStatus, 
                    true, 
                    "Marked contact as read"
            );
        }
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findByIdActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        
        // Store old data for logging
        Contact oldContact = Contact.builder()
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .organisation(contact.getOrganisation())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .isRead(contact.getIsRead())
                .build();
        // Set the ID manually since it's inherited from BaseEntity
        oldContact.setId(contact.getId());
        
        // Perform soft delete
        String currentUser = getCurrentUser();
        contactRepository.softDeleteById(id, LocalDateTime.now(), currentUser);
        
        // Log the operation
        operationLogService.logOperation(
                "contacts", 
                id, 
                OperationLog.OperationType.DELETE, 
                oldContact, 
                null, 
                "Contact soft deleted"
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

    private ContactResponseDto convertToDto(Contact contact) {
        return ContactResponseDto.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .organisation(contact.getOrganisation())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .isRead(contact.getIsRead())
                .createdAt(contact.getCreatedAt())
                .build();
    }
}
