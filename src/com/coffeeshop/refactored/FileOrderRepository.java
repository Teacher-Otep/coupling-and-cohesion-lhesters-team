package com.coffeeshop.refactored;

import java.io.FileWriter;
import java.io.IOException;

public class FileOrderRepository implements OrderRepository {
    private final String filePath;

    public FileOrderRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(Order order, double finalPrice) {
        System.out.println("[System] Saving transaction logs to disk...");
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("Customer: " + order.getCustomerName()
                    + " | Item: " + order.getCoffeeType()
                    + " | Total: " + finalPrice + "\n");
            System.out.println("[Database] Log successfully written to " + filePath);
        } catch (IOException e) {
            System.out.println("[CRITICAL ERROR] Failed to write to file system: " + e.getMessage());
        }
    }
}