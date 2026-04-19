package app.entities;

import java.sql.Date;

public class Orders {
    private int orderId;
    private int userId;
    private String status;
    private Date createdAt;
    private double total;
    private Date pickup;

    public Orders(int orderId, int userId, String status, Date createdAt, double total, Date pickup) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.total = total;
        this.pickup = pickup;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public double getTotal() {
        return total;
    }


    public Date getPickup() {
        return pickup;
    }
}