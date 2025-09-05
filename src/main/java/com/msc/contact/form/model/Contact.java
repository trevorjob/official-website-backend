package com.msc.contact.form.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String organisation;

    @Column(length = 200, nullable = false)
    private String subject;

    @Column(columnDefinition = "text", length = 2000, nullable = false)
    private String message;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_read")
    private boolean isRead = false;


}
