package com.msc.contact.form.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactRequest {
    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 100)
    private String organisation;

    @NotBlank
    @Size(max = 200)
    private String subject;

    @NotBlank
    @Size(max = 2000)
    private String message;
}
