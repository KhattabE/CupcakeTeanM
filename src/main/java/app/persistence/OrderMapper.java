package app.persistence;

import app.entities.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    private ConnectionPool connectionPool;

    public OrderMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public int createOrder(int userId, String status, java.time.LocalDate createdAt, double total, String pickupDate) {
        String sql = """
        INSERT INTO orders (user_id, status, created_at, total, pickup)
        VALUES (?, ?, ?, ?, ?)
        RETURNING order_id
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            preparedStatement.setDate(3, java.sql.Date.valueOf(createdAt));
            preparedStatement.setDouble(4, total);
            preparedStatement.setDate(5, java.sql.Date.valueOf(pickupDate));

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_id");
            }

        } catch (SQLException e) {
            System.out.println("Error in createOrder: " + sql);
        }

        return -1;
    }



    // gets all orders for one user
    public List<Orders> getOrdersByUserId(int userId) {
        String sql = """
        SELECT * FROM orders WHERE user_id = ?
        """;

        List<Orders> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String status = rs.getString("status");
                java.sql.Date createdAt = rs.getDate("created_at");
                double total = rs.getDouble("total");
                java.sql.Date pickup = rs.getDate("pickup");

                Orders order = new Orders(orderId, userId, status, createdAt, total, pickup);
                orders.add(order);
            }

        } catch (SQLException e){
            System.out.println("An error has happened " + sql);
        }

        return orders;
    }

    // gets all orders (admin)
    public List<Orders> getAllOrders() {
        String sql = """
        SELECT * FROM orders
        """;

        List<Orders> orders = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                java.sql.Date createdAt = rs.getDate("created_at");
                double total = rs.getDouble("total");
                java.sql.Date pickup = rs.getDate("pickup");

                Orders order = new Orders(orderId, userId, status, createdAt, total, pickup);
                orders.add(order);
            }

        } catch (SQLException e){
            System.out.println("An error has happened " + sql);
        }

        return orders;
    }

    public void deleteOrderById(int orderId) {
        String deleteOrderLinesSql = "DELETE FROM order_lines WHERE order_id = ?";
        String deleteOrderSql = "DELETE FROM orders WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement psLines = connection.prepareStatement(deleteOrderLinesSql);
                PreparedStatement psOrder = connection.prepareStatement(deleteOrderSql)
        ) {
            psLines.setInt(1, orderId);
            psLines.executeUpdate();

            psOrder.setInt(1, orderId);
            psOrder.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting order with id " + orderId);
        }
    }
    }