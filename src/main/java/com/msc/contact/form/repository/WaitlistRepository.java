package com.msc.contact.form.repository;

import com.msc.contact.form.model.Waitlist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    Optional<Waitlist> findByEmail(@NotBlank @Email @Size(max = 100) String email);
}
