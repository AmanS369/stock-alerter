package com.stockalert.price_producer.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinnhubStockDataDto {
    private double p;  // price
    private String s;  // symbol
    private long t;    // timestamp
    private int v;     // volume
}
