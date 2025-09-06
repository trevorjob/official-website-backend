package com.msc.contact.form.repository;

import com.msc.contact.form.model.AdminUser;
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
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    // Soft delete aware queries
    @Query("SELECT a FROM AdminUser a WHERE a.isDeleted = false")
    List<AdminUser> findAllActive();
    
    @Query("SELECT a FROM AdminUser a WHERE a.isDeleted = false")
    Page<AdminUser> findAllActive(Pageable pageable);
    
    @Query("SELECT a FROM AdminUser a WHERE a.id = :id AND a.isDeleted = false")
    Optional<AdminUser> findByIdActive(@Param("id") Long id);
    
    @Query("SELECT a FROM AdminUser a WHERE a.email = :email AND a.isDeleted = false")
    Optional<AdminUser> findByEmailActive(@Param("email") String email);
    
    // Soft delete operation
    @Modifying
    @Query("UPDATE AdminUser a SET a.isDeleted = true, a.modifiedTime = :modifiedTime, a.modifiedBy = :modifiedBy WHERE a.id = :id")
    void softDeleteById(@Param("id") Long id, @Param("modifiedTime") LocalDateTime modifiedTime, @Param("modifiedBy") String modifiedBy);
    
    // Find deleted records
    @Query("SELECT a FROM AdminUser a WHERE a.isDeleted = true")
    Page<AdminUser> findAllDeleted(Pageable pageable);
}
