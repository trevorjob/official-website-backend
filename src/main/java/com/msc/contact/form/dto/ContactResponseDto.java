package com.msc.contact.form.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContactResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String organisation;
    private String subject;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}
