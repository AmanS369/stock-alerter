package com.stockalert.alert_matcher.service.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublishNotificationAlertDto {

    private String alertId;

    private Double currentPrice;

    private String symbol;

    private String alertType;

}
