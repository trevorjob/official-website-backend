package com.msc.contact.form.repository;

import com.msc.contact.form.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    Page<OperationLog> findByTableNameOrderByOperationTimeDesc(String tableName, Pageable pageable);

    Page<OperationLog> findByOperatorIdOrderByOperationTimeDesc(String operatorId, Pageable pageable);

    Page<OperationLog> findByTableNameAndRecordIdOrderByOperationTimeDesc(String tableName, Long recordId, Pageable pageable);

    @Query("SELECT ol FROM OperationLog ol WHERE ol.operationTime BETWEEN :startTime AND :endTime ORDER BY ol.operationTime DESC")
    Page<OperationLog> findByOperationTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                                 @Param("endTime") LocalDateTime endTime, 
                                                 Pageable pageable);

    List<OperationLog> findByTableNameAndRecordId(String tableName, Long recordId);
}
