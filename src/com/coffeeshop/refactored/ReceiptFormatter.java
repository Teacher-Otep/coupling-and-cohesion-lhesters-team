package com.coffeeshop.refactored;

public interface ReceiptFormatter {
    String format(Order order, double finalPrice);
}