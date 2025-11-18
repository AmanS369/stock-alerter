package com.stockalert.alert_api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponseDto {
    private Long id;
    private String symbol;
    private String condition;
    private double targetPrice;
    private double status;
    private LocalDateTime createdAt;
}
