package com.stockalert.alert_api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {

    private String symbol;


    private String condition; // ABOVE, BELOW


    private double targetPrice;


    private String status ;


    private LocalDateTime createdAt;


    private LocalDateTime triggeredAt;


    private LocalDateTime notifiedAt;
}
