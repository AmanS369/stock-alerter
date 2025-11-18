package com.stockalert.alert_matcher.service.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TriggeredAlertBatchDto {
    private String processingKey;
    private Set<ZSetOperations.TypedTuple<String>> alerts;
}

