package com.msc.contact.form.controller;

import com.msc.contact.form.dto.ResponseDto;
import com.msc.contact.form.dto.WaitlistRequest;
import com.msc.contact.form.service.WaitlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/waitlist")
@RequiredArgsConstructor
public class WaitlistController {
    private final WaitlistService waitlistService;


    @PostMapping
    public ResponseEntity<ResponseDto> joinWaitlist(@Valid @RequestBody WaitlistRequest dto) {
        return ResponseEntity.ok(waitlistService.joinWaitlist(dto));
    }
}
