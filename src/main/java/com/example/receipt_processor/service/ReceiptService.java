package com.example.receipt_processor.service;

import com.example.receipt_processor.model.Item;
import com.example.receipt_processor.model.Receipt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ReceiptService {

    private Map<String, Integer> receiptPoints = new HashMap<>();


    public String processReceipt(Receipt receipt) {
        int points = calculatePoints(receipt);
        String id = UUID.randomUUID().toString();
        receiptPoints.put(id, points);
        return id;
    }


    public Integer getPoints(String id) {
        return receiptPoints.get(id);
    }


    private int calculatePoints(Receipt receipt) {
        int points = 0;


        String retailer = receipt.getRetailer();
        for (char c : retailer.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                points += 1;
            }
        }


        BigDecimal total = new BigDecimal(receipt.getTotal());


        if (total.scale() == 2 && total.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
        }


        BigDecimal q = new BigDecimal("0.25");
        if (total.remainder(q).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }


        int itemCount = receipt.getItems().size();
        points += (itemCount / 2) * 5;


        for (Item item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                BigDecimal price = new BigDecimal(item.getPrice());
                BigDecimal bonus = price.multiply(new BigDecimal("0.2"));
                bonus = bonus.setScale(0, RoundingMode.CEILING);
                points += bonus.intValue();
            }
        }



        LocalDate purchaseDate = LocalDate.parse(receipt.getPurchaseDate());
        if (purchaseDate.getDayOfMonth() % 2 == 1) {
            points += 6;
        }


        LocalTime purchaseTime = LocalTime.parse(receipt.getPurchaseTime());
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(16, 0);
        if (purchaseTime.compareTo(start) >= 0 && purchaseTime.compareTo(end) < 0) {
            points += 10;
        }

        return points;
    }
}
