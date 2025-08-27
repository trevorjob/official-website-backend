package com.msc.contact.form.service.implementation;

import com.msc.contact.form.dto.ContactRequest;
import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.model.Contact;
import com.msc.contact.form.repository.ContactRepository;
import com.msc.contact.form.service.ContactService;
import lombok.RequiredArgsConstructor;
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


}
