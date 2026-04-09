package app.entities;

public class CupcakeTop {

    private int cupcakeTopId;
    private String topping;
    private double price;

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

    public void setCupcakeTopId(int cupcakeTopId) {
        this.cupcakeTopId = cupcakeTopId;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
