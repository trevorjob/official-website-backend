package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.ContactRequest;
import com.msc.contact.form.dto.ContactResponseDto;
import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.exception.ResourceNotFoundException;
import com.msc.contact.form.model.Contact;
import com.msc.contact.form.repository.ContactRepository;
import com.msc.contact.form.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    @Override
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

        return ResponseDto.builder()
                .id(saved.getId())
                .message("Contact form submitted successfully")
                .build();
    }

    @Override
    public ContactResponseDto getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        return convertToDto(contact);
    }

    @Override
    public Page<ContactResponseDto> getAllContacts(PageRequest pageRequest) {
        return contactRepository.findAll(pageRequest)
                .map(this::convertToDto);
    }

    @Override
    public void markAsRead(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        if (!contact.isRead()) {
            contact.setRead(true);
            contact = contactRepository.save(contact);
        }

        convertToDto(contact);
    }

    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contactRepository.delete(contact);
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
                .isRead(contact.isRead())
                .createdAt(contact.getCreatedAt())
                .build();
    }

}
