package com.msc.contact.form.controller;

import com.msc.contact.form.dto.ContactRequest;
import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ResponseDto> submitContact(@Valid @RequestBody ContactRequest dto) {
        return ResponseEntity.ok(contactService.submitContact(dto));
    }
}
