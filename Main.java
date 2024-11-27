import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    private Map<String, Integer> unitPrices = new HashMap<>();
    private Map<String, SpecialPrice> specialPrices = new HashMap<>();
    private Map<String, Integer> itemCounts = new HashMap<>();
    private int total = 0;

    // SpecialPrice class (nested class)
    private class SpecialPrice {
        int quantity;
        int price;

        public SpecialPrice(int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }
    }

    // Add pricing rules
    public void setPricing(String sku, int unitPrice, int specialQuantity, int specialPrice) {
        unitPrices.put(sku, unitPrice);
        if (specialQuantity > 0 && specialPrice > 0) {
            specialPrices.put(sku, new SpecialPrice(specialQuantity, specialPrice));
        }
    }

    // Scan an item
    public void scan(String sku) {
        itemCounts.put(sku, itemCounts.getOrDefault(sku, 0) + 1);
        total = calculateTotal();
        System.out.println("Running total after scanning " + sku + ": " + total + " pence");
    }

    // Calculate total price
    private int calculateTotal() {
        int total = 0;
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            String sku = entry.getKey();
            int count = entry.getValue();

            if (specialPrices.containsKey(sku)) {
                SpecialPrice specialPrice = specialPrices.get(sku);
                int specialSets = count / specialPrice.quantity;
                int remainder = count % specialPrice.quantity;
                total += specialSets * specialPrice.price;
                total += remainder * unitPrices.get(sku);
            } else {
                total += count * unitPrices.get(sku);
            }
        }
        return total;
    }

    // Get final total
    public int getTotal() {
        return total;
    }

    // Main method
    public static void main(String[] args) {
        Main checkout = new Main();

        // Set pricing rules
        checkout.setPricing("A", 50, 3, 130);
        checkout.setPricing("B", 30, 2, 45);
        checkout.setPricing("C", 20, 0, 0);
        checkout.setPricing("D", 15, 0, 0);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter SKUs to scan (e.g., A, B, C). Type 'DONE' to finish scanning.");

        while (true) {
            String input = scanner.nextLine().toUpperCase();
            if (input.equals("DONE")) {
                break;
            }
            if (checkout.unitPrices.containsKey(input)) {
                checkout.scan(input);
            } else {
                System.out.println("Invalid SKU. Please try again.");
            }
        }

        System.out.println("Final total: " + checkout.getTotal() + " pence");
        scanner.close();
    }
}
