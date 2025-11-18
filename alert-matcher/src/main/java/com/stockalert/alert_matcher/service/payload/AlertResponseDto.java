package com.stockalert.alert_matcher.service.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertResponseDto {
    private String symbol;

    private long unixTimeStamp;

    private double lastPrice;

    private int volume;

}
