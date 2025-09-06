package com.msc.contact.form.controller;

import com.msc.contact.form.model.OperationLog;
import com.msc.contact.form.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/logs")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogRepository operationLogRepository;

    @GetMapping
    public ResponseEntity<Page<OperationLog>> getAllOperationLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "operationTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String operatorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OperationLog> logs;

        if (startTime != null && endTime != null) {
            logs = operationLogRepository.findByOperationTimeBetween(startTime, endTime, pageRequest);
        } else if (tableName != null && !tableName.isEmpty()) {
            logs = operationLogRepository.findByTableNameOrderByOperationTimeDesc(tableName, pageRequest);
        } else if (operatorId != null && !operatorId.isEmpty()) {
            logs = operationLogRepository.findByOperatorIdOrderByOperationTimeDesc(operatorId, pageRequest);
        } else {
            logs = operationLogRepository.findAll(pageRequest);
        }

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{tableName}/{recordId}")
    public ResponseEntity<Page<OperationLog>> getOperationLogsByRecord(
            @PathVariable String tableName,
            @PathVariable Long recordId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        Page<OperationLog> logs = operationLogRepository.findByTableNameAndRecordIdOrderByOperationTimeDesc(
                tableName, recordId, pageRequest);

        return ResponseEntity.ok(logs);
    }
}
