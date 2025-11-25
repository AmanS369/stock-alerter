package com.stockalert.alert_api.controller;

import com.stockalert.alert_api.payload.AlertDto;
import com.stockalert.alert_api.payload.CreateAlertDto;
import com.stockalert.alert_api.service.AlertService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert-api/v1")
@AllArgsConstructor
public class AlertController {

    private final AlertService alertService;
    @PostMapping("/create-alert")
    public ResponseEntity<String> createTask(@RequestBody CreateAlertDto alertDto) {
        alertService.createAlert(alertDto);
        return ResponseEntity.ok("DOne");
    }

    @GetMapping("/alert")
    public ResponseEntity<List<AlertDto>> getAlert() {
        List<AlertDto> savedAlert = alertService.getAllAlert();
        return ResponseEntity.ok(savedAlert);
    }

}
