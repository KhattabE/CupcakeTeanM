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

    public int getOrderId() {
        return orderId;
    }

    public int getCupcakeId() {
        return cupcakeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

}
