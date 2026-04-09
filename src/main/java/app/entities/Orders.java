package app.entities;

import javax.xml.crypto.Data;

public class Orders {
   private int orderId;
   private int userId;
   private String status;
   private Data createdAt;
   private double total;


    public Orders(int orderId, int userId, String status, Data createdAt, double total) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.total = total;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Data createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
