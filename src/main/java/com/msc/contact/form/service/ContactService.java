package com.msc.contact.form.service;

import com.msc.contact.form.dto.ContactRequest;
import com.msc.contact.form.dto.ContactResponseDto;
import com.msc.contact.form.dto.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ContactService {
    ResponseDto submitContact(ContactRequest dto);
    ContactResponseDto getContactById(Long id);
    Page<ContactResponseDto> getAllContacts(PageRequest pageRequest);
    Page<ContactResponseDto> getDeletedContacts(PageRequest pageRequest);
    void markAsRead(Long id);
    void deleteContact(Long id);
}
