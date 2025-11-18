package com.stockalert.price_producer.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Component
@ConfigurationProperties(prefix = "stock-api")
public class StockProperties {
    private List<String> stocksList;
}
