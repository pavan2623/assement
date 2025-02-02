package com.example.receipt_processor.controller;

import com.example.receipt_processor.model.Receipt;
import com.example.receipt_processor.service.ReceiptService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;


    @PostMapping("/receipts/process")
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt) {
        try {
            String id = receiptService.processReceipt(receipt);
            return ResponseEntity.ok(Collections.singletonMap("id", id));
        } catch (Exception e) {
            // Return a 400 Bad Request with a message containing "Please verify input."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Please verify input. " + e.getMessage()));
        }
    }


    @GetMapping("/receipts/{id}/points")
    public ResponseEntity<Map<String, Integer>> getPoints(@PathVariable String id) {
        Integer points = receiptService.getPoints(id);
        if (points == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(Collections.singletonMap("points", points));
    }
}
