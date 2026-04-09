package app.entities;

public class OrderLine {
    private int orderLineId;
    private int orderId;
    private int cupcakeId;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public OrderLine(int orderLineId, int orderId, int cupcakeId, int quantity, double unitPrice, double lineTotal) {
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.cupcakeId = cupcakeId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public int getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCupcakeId() {
        return cupcakeId;
    }

    public void setCupcakeId(int cupcakeId) {
        this.cupcakeId = cupcakeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
