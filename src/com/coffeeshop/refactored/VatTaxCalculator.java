package com.coffeeshop.refactored;

public class VatTaxCalculator implements TaxCalculator {
    private final double taxRate;

    public VatTaxCalculator(double taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public double calculateFinalPrice(double basePrice) {
        return basePrice + (basePrice * taxRate);
    }
}