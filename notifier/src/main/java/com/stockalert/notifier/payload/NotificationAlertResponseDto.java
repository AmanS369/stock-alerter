package com.stockalert.notifier.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationAlertResponseDto {
    private String alertId;

    private Double currentPrice;

    private String symbol;

    private String alertType;

}
