package com.stockalert.price_producer.priceconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockalert.price_producer.config.StockProperties;
import com.stockalert.price_producer.payload.FinnhubStockDataDto;
import com.stockalert.price_producer.payload.FinnhubTradeResponseDto;
import com.stockalert.price_producer.payload.StockPriceDto;
import com.stockalert.price_producer.service.StockKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockWebSocketHandler extends TextWebSocketHandler {

    private final StockKafkaProducer producer;

    private final ObjectMapper objectMapper;

 //   @Value("${stock-api.stocks}")
    private final StockProperties stockProperties;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established with provider.");

        for (String symbol : stockProperties.getStocksList()) {
            String message = String.format("{\"type\":\"subscribe\",\"symbol\":\"%s\"}", symbol);
            session.sendMessage(new TextMessage(message));
            log.info("Subscribed to: {}", symbol);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)  {
        try {
            FinnhubTradeResponseDto response =
                    objectMapper.readValue(message.getPayload(), FinnhubTradeResponseDto.class);

            if (!"trade".equals(response.getType())) {
                return;
            }

            for (FinnhubStockDataDto trade : response.getData()) {

                producer.publish(StockPriceDto.builder()
                                .lastPrice(trade.getP())
                                .symbol(trade.getS())
                                .unixTimeStamp(trade.getT())
                                .volume(trade.getV())
                        .build());
            }

        } catch (Exception e) {
            log.error("Error parsing websocket message: {}", message.getPayload(), e);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("WebSocket transport error:{} ", exception.getMessage());
        // TODO: Decide reconnection or session cleanup strategy
    }
}
