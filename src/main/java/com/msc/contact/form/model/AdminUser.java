package com.msc.contact.form.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin_users")
public class AdminUser extends BaseEntity {

    @Column(length = 200, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}
