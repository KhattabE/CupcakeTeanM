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
            INSERT INTO orders (user_id, status, created_at, total) VALUES (?,?,?,?)
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.setDate(3, order.getCreatedAt());
            preparedStatement.setDouble(4, order.getTotal());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
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

                return new Orders(orderId, userId, status, createdAt, total);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
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

                Orders order = new Orders(orderId, userId, status, createdAt, total);
                orders.add(order);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
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

                Orders order = new Orders(orderId, userId, status, createdAt, total);
                orders.add(order);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
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
}