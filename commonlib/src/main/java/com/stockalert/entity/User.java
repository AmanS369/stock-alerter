package com.stockalert.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // matches SERIAL
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(length = 255)
    private String name;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
