package com.msc.contact.form.controller;

import com.msc.contact.form.dto.AdminStatsResponse;
import com.msc.contact.form.dto.ContactResponseDto;
import com.msc.contact.form.dto.WaitlistResponseDto;
import com.msc.contact.form.service.AdminStatService;
import com.msc.contact.form.service.ContactService;
import com.msc.contact.form.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ContactService contactService;
    private final WaitlistService waitlistService;
    private final AdminStatService adminStatService;

    @GetMapping("/contact/{id}")
    public ResponseEntity<ContactResponseDto> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @GetMapping("/contacts")
    public ResponseEntity<Page<ContactResponseDto>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        Page<ContactResponseDto> contacts = contactService.getAllContacts(pageRequest);
        return ResponseEntity.ok(contacts);
    }
    @GetMapping("/waitlists")
    public ResponseEntity<Page<WaitlistResponseDto>> getAllWaitlists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder){
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(direction, sortBy));
        Page<WaitlistResponseDto> waitlists = waitlistService.getWaitlists(pageRequest);
        return ResponseEntity.ok(waitlists);
    }
    @PatchMapping("/contacts/{id}/read")
    public ResponseEntity<Void> markContactAsRead(@PathVariable Long id) {
        contactService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/waitlist/{id}")
    public ResponseEntity<Void> deleteWaitlist(@PathVariable Long id) {
        waitlistService.deleteWaitlist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getAdminStats() {
        return ResponseEntity.ok(adminStatService.getAdminStats());
    }

}
