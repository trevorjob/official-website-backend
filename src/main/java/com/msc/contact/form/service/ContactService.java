package com.msc.contact.form.service;

import com.msc.contact.form.dto.ContactRequest;
import com.msc.contact.form.dto.ResponseDto;

public interface ContactService {
    ResponseDto submitContact(ContactRequest dto);
}
