package com.msc.contact.form.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "operation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name", length = 50, nullable = false)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "operation_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "operator_id", length = 100)
    private String operatorId;

    @Column(name = "operator_name", length = 100)
    private String operatorName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "operation_description", length = 500)
    private String operationDescription;

    @CreationTimestamp
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    public enum OperationType {
        CREATE,   // 创建
        UPDATE,   // 更新
        DELETE,   // 删除 (软删除)
        READ      // 查询 (如果需要记录查询操作)
    }
}
