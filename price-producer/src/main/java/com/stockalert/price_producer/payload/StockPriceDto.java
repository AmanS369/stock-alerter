package com.stockalert.price_producer.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceDto {
    private String symbol;

    private long unixTimeStamp;

    private double lastPrice;

    private int volume;

}
