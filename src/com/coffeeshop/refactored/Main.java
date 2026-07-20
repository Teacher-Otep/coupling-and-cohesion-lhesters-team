package com.coffeeshop.refactored;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starting Refactored Coffee Shop System ===");

        TaxCalculator taxCalculator = new VatTaxCalculator(0.12); // 12% VAT
        ReceiptFormatter receiptFormatter = new PlainTextReceiptFormatter("PHP");
        OrderRepository orderRepository = new FileOrderRepository("orders_log.txt");
        OrderProcessor processor = new OrderProcessor(taxCalculator, receiptFormatter, orderRepository);

        // Regular customer order
        Order order1 = new Order("Juan Dela Cruz", "Java Chip Frappe", 150.0);
        processor.processOrder(order1, new NoDiscountPolicy());

        // Senior citizen order gets a 20% discount before tax
        Order order2 = new Order("Lola Rosa Santos", "Brewed Coffee", 90.0);
        processor.processOrder(order2, new SeniorCitizenDiscountPolicy());

        System.out.println("\n=== Order Processing Complete ===");
    }
}