package com.stockalert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 10, nullable = false)
    private String symbol;

    @Column(length = 20, nullable = false)
    private String condition; // ABOVE, BELOW

    @Column(name = "target_price", precision = 10, nullable = false)
    private double targetPrice;

    @Column(length = 20)
    private String status = "active"; // active, triggered, notified

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;

    @Column(name = "notified_at")
    private LocalDateTime notifiedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
