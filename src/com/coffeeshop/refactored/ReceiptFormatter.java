package com.coffeeshop.refactored;

public interface ReceiptFormatter {
    String format(Order order, double subtotal, double discountAmount, double finalPrice);
}