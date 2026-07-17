package com.coffeeshop.refactored;

public class PlainTextReceiptFormatter implements ReceiptFormatter {
    private final String currencySymbol;

    public PlainTextReceiptFormatter(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public String format(Order order, double finalPrice) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== COFFEE SHOP RECEIPT =====\n");
        sb.append("Customer: ").append(order.getCustomerName()).append("\n");
        sb.append("Beverage: ").append(order.getCoffeeType()).append("\n");
        sb.append("Total Amount (incl. Tax): ").append(currencySymbol).append(" ").append(finalPrice).append("\n");
        sb.append("================================\n");
        return sb.toString();
    }
}