package com.msc.contact.form.repository;

import com.msc.contact.form.model.Waitlist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    
    // Soft delete aware queries
    @Query("SELECT w FROM Waitlist w WHERE w.isDeleted = false")
    List<Waitlist> findAllActive();
    
    @Query("SELECT w FROM Waitlist w WHERE w.isDeleted = false")
    Page<Waitlist> findAllActive(Pageable pageable);
    
    @Query("SELECT w FROM Waitlist w WHERE w.id = :id AND w.isDeleted = false")
    Optional<Waitlist> findByIdActive(@Param("id") Long id);
    
    @Query("SELECT w FROM Waitlist w WHERE w.email = :email AND w.isDeleted = false")
    Optional<Waitlist> findByEmailActive(@Param("email") @NotBlank @Email @Size(max = 100) String email);
    
    @Query("SELECT COUNT(w) FROM Waitlist w WHERE w.createdAt >= :date AND w.isDeleted = false")
    long countByCreatedAtGreaterThanEqual(@Param("date") LocalDateTime date);
    
    // Soft delete operation
    @Modifying
    @Query("UPDATE Waitlist w SET w.isDeleted = true, w.modifiedTime = :modifiedTime, w.modifiedBy = :modifiedBy WHERE w.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("modifiedTime") LocalDateTime modifiedTime, @Param("modifiedBy") String modifiedBy);
    
    // Find deleted records
    @Query("SELECT w FROM Waitlist w WHERE w.isDeleted = true")
    Page<Waitlist> findAllDeleted(Pageable pageable);
    
    // Find any record by email (including deleted ones)
    @Query("SELECT w FROM Waitlist w WHERE w.email = :email")
    Optional<Waitlist> findByEmailIncludingDeleted(@Param("email") String email);
    
    // Restore a soft deleted record
    @Modifying
    @Query("UPDATE Waitlist w SET w.isDeleted = false, w.modifiedTime = :modifiedTime, w.modifiedBy = :modifiedBy WHERE w.id = :id")
    void restoreById(@Param("id") Long id, @Param("modifiedTime") LocalDateTime modifiedTime, @Param("modifiedBy") String modifiedBy);
}
