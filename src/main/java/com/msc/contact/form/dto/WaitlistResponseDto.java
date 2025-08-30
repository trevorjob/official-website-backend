package com.msc.contact.form.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WaitlistResponseDto {
    private Long id;
    private String email;
    private LocalDateTime createdAt;
}
