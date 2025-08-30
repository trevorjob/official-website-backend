package com.msc.contact.form.repository;

import com.msc.contact.form.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    long countByIsReadTrue();
    long countByIsReadFalse();
    long countByCreatedAtGreaterThanEqual(LocalDateTime date);

}
