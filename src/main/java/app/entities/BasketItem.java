package app.entities;

public class BasketItem {

    private String topping;
    private String bottom;
    private int quantity;
    private double unitPrice;

    public BasketItem(String topping, String bottom, int quantity, double unitPrice) {
        this.topping = topping;
        this.bottom = bottom;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getTopping() {
        return topping;
    }

    public String getBottom() {
        return bottom;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}