package assignment;

public class OrderCalculator {

    public double calculateTotalPrice(Order order) {
        double total = 0.0;

        for (Item item : order.getItems()) {
            double itemPrice = applyDiscount(item);
            itemPrice = applyTax(item, itemPrice);
            total += itemPrice * item.getQuantity();
        }

        total = applyOrderDiscounts(order, total);

        return total;
    }

    private double applyDiscount(Item item) {
        double price = item.getPrice();
        if (item.getDiscountType() != null) {
            if (item.getDiscountType() == DiscountType.PERCENTAGE) {
                price -= item.getDiscountAmount() * price;
            } else if (item.getDiscountType() == DiscountType.AMOUNT) {
                price -= item.getDiscountAmount();
            } else {
                // No discount applied
            }
        }
        return price;
    }

        private double applyTax(Item item, double price) {
            if (item instanceof TaxableItem) {
                TaxableItem taxableItem = (TaxableItem) item;
                return price + (taxableItem.getTaxRate() / 100.0 * price);
            }
            return price;
        }

        private double applyOrderDiscounts(Order order, double total) {
            if (order.hasGiftCard()) {
                total -= 10.0; // Subtract $10 for gift card
            }
            if (total > 100.0) {
                total *= 0.9; // Apply 10% discount for orders over $100
            }
            return total;
        }


}
