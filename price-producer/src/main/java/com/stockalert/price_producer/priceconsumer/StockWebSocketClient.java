package com.stockalert.price_producer.priceconsumer;

import com.stockalert.price_producer.service.StockKafkaProducer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockWebSocketClient {

    private final StockKafkaProducer producer;

    private WebSocketConnectionManager connectionManager;

    private final WebSocketHandler webSocketHandler;

    @Value("${stock-api.base-url}")
    private  String WS_URL;

    @PostConstruct
    public void connect() {
        try {
            StandardWebSocketClient client = new StandardWebSocketClient();
            connectionManager = new WebSocketConnectionManager(client, webSocketHandler, WS_URL);
            connectionManager.setAutoStartup(true);
            connectionManager.start();

            log.info("Connected to WebSocket");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Something went wrong " ,e);
        }
    }

    @PreDestroy
    public void disconnect() {
        if (connectionManager != null && connectionManager.isRunning()) {
            connectionManager.stop();
            log.info("Disconnected from WebSocket");
        }
    }
}