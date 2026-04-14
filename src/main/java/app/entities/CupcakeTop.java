package app.entities;

public class CupcakeTop {
    private final int cupcakeTopId;
    private final String topping;
    private final double price;

    public CupcakeTop(int cupcakeTopId, String topping, double price) {
        this.cupcakeTopId = cupcakeTopId;
        this.topping = topping;
        this.price = price;
    }

    public int getCupcakeTopId() {
        return cupcakeTopId;
    }

    public String getTopping() {
        return topping;
    }

    public double getPrice() {
        return price;
    }
}
