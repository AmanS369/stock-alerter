package com.stockalert.notifier.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmailBodyDto {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
