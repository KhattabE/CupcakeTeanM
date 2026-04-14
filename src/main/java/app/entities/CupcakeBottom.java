package app.entities;

public class CupcakeBottom {
    private final int cupcakeBottomId;
    private final String bottom;
    private final double price;

    public CupcakeBottom(int cupcakeBottomId, String bottom, double price) {
        this.cupcakeBottomId = cupcakeBottomId;
        this.bottom = bottom;
        this.price = price;
    }

    public int getCupcakeBottomId() {
        return cupcakeBottomId;
    }

    public String getBottom() {
        return bottom;
    }

    public double getPrice() {
        return price;
    }
}
