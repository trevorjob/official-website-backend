package com.msc.contact.form.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "waitlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Waitlist extends BaseEntity {

    @Column(length = 100, nullable = false, unique = true)
    private String email;

}
