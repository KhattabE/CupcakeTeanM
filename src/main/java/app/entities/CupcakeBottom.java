package app.entities;

public class CupcakeBottom {

    private int cupcakeBottomId;
    private String bottom;
    private double price;

    public CupcakeBottom(int cupcakeBottomId, String bottom, double price) {
        this.cupcakeBottomId = cupcakeBottomId;
        this.bottom = bottom;
        this.price = price;
    }

    public int getCupcakeBottomId() {
        return cupcakeBottomId;
    }

    public void setCupcakeBottomId(int cupcakeBottomId) {
        this.cupcakeBottomId = cupcakeBottomId;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
