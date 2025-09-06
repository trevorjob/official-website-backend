package com.msc.contact.form.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.msc.contact.form.model.OperationLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogResponseDto {
    private Long id;
    private String tableName;
    private Long recordId;
    private OperationLog.OperationType operationType;
    private String operatorId;
    private String operatorName;
    private String oldValue;
    private String newValue;
    private String operationDescription;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
    private String ipAddress;
    private String userAgent;
}
