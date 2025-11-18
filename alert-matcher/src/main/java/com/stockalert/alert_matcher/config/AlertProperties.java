package com.stockalert.alert_matcher.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "alert")
public class AlertProperties {
    private List<String> typeList;
}
