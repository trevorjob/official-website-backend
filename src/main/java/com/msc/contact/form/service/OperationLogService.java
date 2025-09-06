package com.msc.contact.form.service;

import com.msc.contact.form.model.OperationLog;
import com.msc.contact.form.repository.OperationLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;
    private final ObjectMapper objectMapper;

    public void logOperation(String tableName, Long recordId, OperationLog.OperationType operationType, 
                           Object oldValue, Object newValue, String description) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String operatorId = "system";
            String operatorName = "System";
            
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getPrincipal())) {
                operatorId = authentication.getName();
                operatorName = authentication.getName();
            }

            HttpServletRequest request = getCurrentHttpRequest();
            String ipAddress = getClientIpAddress(request);
            String userAgent = request != null ? request.getHeader("User-Agent") : null;

            OperationLog log = OperationLog.builder()
                    .tableName(tableName)
                    .recordId(recordId)
                    .operationType(operationType)
                    .operatorId(operatorId)
                    .operatorName(operatorName)
                    .oldValue(convertToJson(oldValue))
                    .newValue(convertToJson(newValue))
                    .operationDescription(description)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();

            operationLogRepository.save(log);
            
        } catch (Exception e) {
            log.error("Failed to log operation: {}", e.getMessage(), e);
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private String convertToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert object to JSON: {}", e.getMessage());
            return obj.toString();
        }
    }
}
