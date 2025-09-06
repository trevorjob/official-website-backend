package com.msc.contact.form.repository;

import com.msc.contact.form.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    // Soft delete aware queries - only return non-deleted records
    @Query("SELECT c FROM Contact c WHERE c.isDeleted = false")
    List<Contact> findAllActive();
    
    @Query("SELECT c FROM Contact c WHERE c.isDeleted = false")
    Page<Contact> findAllActive(Pageable pageable);
    
    @Query("SELECT c FROM Contact c WHERE c.id = :id AND c.isDeleted = false")
    Optional<Contact> findByIdActive(@Param("id") Long id);
    
    // Statistics queries with soft delete awareness
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.isRead = true AND c.isDeleted = false")
    long countByIsReadTrue();
    
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.isRead = false AND c.isDeleted = false")
    long countByIsReadFalse();
    
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.createdAt >= :date AND c.isDeleted = false")
    long countByCreatedAtGreaterThanEqual(@Param("date") LocalDateTime date);
    
    // Soft delete operation
    @Modifying
    @Query("UPDATE Contact c SET c.isDeleted = true, c.modifiedTime = :modifiedTime, c.modifiedBy = :modifiedBy WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("modifiedTime") LocalDateTime modifiedTime, @Param("modifiedBy") String modifiedBy);
    
    // Find deleted records (for admin recovery purposes)
    @Query("SELECT c FROM Contact c WHERE c.isDeleted = true")
    Page<Contact> findAllDeleted(Pageable pageable);
    
    // Find any record by email (including deleted ones) - useful for analytics
    @Query("SELECT c FROM Contact c WHERE c.email = :email")
    List<Contact> findByEmailIncludingDeleted(@Param("email") String email);
}
