package app.persistence;

import app.entities.OrderLine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderLineMapper {
    private ConnectionPool connectionPool;

    public OrderLineMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // adds a cupcake line to an order
    public void addOrderLine(OrderLine orderLine) {
        String sql = """
            INSERT INTO order_lines (order_id, cupcake_id, quantity, unit_price, line_total) VALUES (?,?,?,?,?)
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, orderLine.getOrderId());
            preparedStatement.setInt(2, orderLine.getCupcakeId());
            preparedStatement.setInt(3, orderLine.getQuantity());
            preparedStatement.setDouble(4, orderLine.getUnitPrice());
            preparedStatement.setDouble(5, orderLine.getLineTotal());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // gets all order lines for one order
    public List<OrderLine> getOrderLinesByOrderId(int orderId) {
        String sql = """
            SELECT * FROM order_lines WHERE order_id = ?
            """;

        List<OrderLine> orderLines = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, orderId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int orderLineId = rs.getInt("order_line_id");
                int cupcakeId = rs.getInt("cupcake_id");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");
                double lineTotal = rs.getDouble("line_total");

                OrderLine orderLine = new OrderLine(orderLineId, orderId, cupcakeId, quantity, unitPrice, lineTotal);
                orderLines.add(orderLine);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return orderLines;
    }

    // removes one order line by its id
    public void deleteOrderLine(int orderLineId) {
        String sql = """
            DELETE FROM order_lines WHERE order_line_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, orderLineId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // updates quantity for one order line
    public void updateQuantity(int orderLineId, int quantity) {
        String sql = """
            UPDATE order_lines SET quantity = ? WHERE order_line_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, orderLineId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // gets one order line by its id
    public OrderLine getOrderLineById(int orderLineId) {
        String sql = """
            SELECT * FROM order_lines WHERE order_line_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, orderLineId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                int cupcakeId = rs.getInt("cupcake_id");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");
                double lineTotal = rs.getDouble("line_total");

                return new OrderLine(orderLineId, orderId, cupcakeId, quantity, unitPrice, lineTotal);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }

    public List<String> getOrderLineDescriptionsByOrderId(int orderId) {
        String sql = """
        SELECT ol.quantity, b.bottom, t.topping
        FROM order_lines ol
        JOIN cupcake c ON ol.cupcake_id = c.cupcake_id
        JOIN "cupcakeBottom" b ON c.cupcake_bottom_id = b.cupcake_bottom_id
        JOIN "cupcakeTop" t ON c.cupcake_top_id = t.cupcake_top_id
        WHERE ol.order_id = ?
        ORDER BY ol.order_line_id
        """;

        List<String> descriptions = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, orderId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                String bottom = rs.getString("bottom");
                String topping = rs.getString("topping");

                descriptions.add(quantity + "x " + bottom + " + " + topping + " topping");
            }

        } catch (SQLException e) {
            System.out.println("An error has happened in getOrderLineDescriptionsByOrderId: " + sql);
        }

        return descriptions;
    }
}