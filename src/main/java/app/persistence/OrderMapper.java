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

    // creates a new order (cart)
    public void createOrder(Orders order) {
        String sql = """
        INSERT INTO orders (user_id, status, created_at, total, pickup) VALUES (?,?,?,?,?)
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.setDate(3, order.getCreatedAt());
            preparedStatement.setDouble(4, order.getTotal());
            preparedStatement.setDate(5, order.getPickup());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has happened " + sql);
        }
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

    // gets an order by id
    public Orders getOrderById(int orderId) {
        String sql = """
        SELECT * FROM orders WHERE order_id = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, orderId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String status = rs.getString("status");
                java.sql.Date createdAt = rs.getDate("created_at");
                double total = rs.getDouble("total");
                java.sql.Date pickup = rs.getDate("pickup");

                return new Orders(orderId, userId, status, createdAt, total, pickup);
            }

        } catch (SQLException e){
            System.out.println("An error has happened " + sql);
        }

        return null;
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

    // updates order status (cart -> paid)
    public void updateOrderStatus(int orderId, String status) {
        String sql = """
            UPDATE orders SET status = ? WHERE order_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, orderId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // updates total price of order
    public void updateOrderTotal(int orderId, double total) {
        String sql = """
            UPDATE orders SET total = ? WHERE order_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setDouble(1, total);
            preparedStatement.setInt(2, orderId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    public void deleteOrderById(int orderId) {
        String deleteOrderLinesSql = "DELETE FROM order_lines WHERE order_id = ?";
        String deleteOrderSql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection conn = connectionPool.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psLines = conn.prepareStatement(deleteOrderLinesSql);
                 PreparedStatement psOrder = conn.prepareStatement(deleteOrderSql)) {

                psLines.setInt(1, orderId);
                psLines.executeUpdate();

                psOrder.setInt(1, orderId);
                psOrder.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Error deleting order with id " + orderId, e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error while deleting order", e);
        }
    }


}