package app.persistence;

import app.entities.Cupcake;
import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {
    private ConnectionPool connectionPool;

    public CupcakeMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }


    public List<CupcakeTop> getAllToppings() {
        List<CupcakeTop> toppings = new ArrayList<>();

        String sql = """
            SELECT cupcake_top_id, topping, price
            FROM "cupcakeTop"
            ORDER BY cupcake_top_id
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("cupcake_top_id");
                String topping = rs.getString("topping");
                double price = rs.getDouble("price");

                toppings.add(new CupcakeTop(id, topping, price));
            }

        } catch (SQLException e) {
            System.out.println("An error has happened in getAllToppings: " + e.getMessage());
        }

        return toppings;
    }

    public List<CupcakeBottom> getAllBottoms() {
        List<CupcakeBottom> bottoms = new ArrayList<>();

        String sql = """
            SELECT cupcake_bottom_id, bottom, price
            FROM "cupcakeBottom"
            ORDER BY cupcake_bottom_id
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("cupcake_bottom_id");
                String bottom = rs.getString("bottom");
                double price = rs.getDouble("price");

                bottoms.add(new CupcakeBottom(id, bottom, price));
            }

        } catch (SQLException e) {
            System.out.println("An error has happened in getAllBottoms: " + e.getMessage());
        }

        return bottoms;
    }

    public CupcakeTop getToppingById(int cupcakeTopId) {
        String sql = """
            SELECT cupcake_top_id, topping, price
            FROM "cupcakeTop"
            WHERE cupcake_top_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, cupcakeTopId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("cupcake_top_id");
                String topping = rs.getString("topping");
                double price = rs.getDouble("price");

                return new CupcakeTop(id, topping, price);
            }

        } catch (SQLException e) {
            System.out.println("An error has happened in getToppingById: " + e.getMessage());
        }

        return null;
    }

    public CupcakeBottom getBottomById(int cupcakeBottomId) {
        String sql = """
            SELECT cupcake_bottom_id, bottom, price
            FROM "cupcakeBottom"
            WHERE cupcake_bottom_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, cupcakeBottomId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("cupcake_bottom_id");
                String bottom = rs.getString("bottom");
                double price = rs.getDouble("price");

                return new CupcakeBottom(id, bottom, price);
            }

        } catch (SQLException e) {
            System.out.println("An error has happened in getBottomById: " + e.getMessage());
        }

        return null;
    }

    public int getOrCreateCupcakeId(String bottomName, String toppingName) {
        String findSql = """
        SELECT c.cupcake_id
        FROM cupcake c
        JOIN "cupcakeBottom" b ON c.cupcake_bottom_id = b.cupcake_bottom_id
        JOIN "cupcakeTop" t ON c.cupcake_top_id = t.cupcake_top_id
        WHERE b.bottom = ? AND t.topping = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(findSql)
        ) {
            preparedStatement.setString(1, bottomName);
            preparedStatement.setString(2, toppingName);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("cupcake_id");
            }

        } catch (SQLException e) {
            System.out.println("Error in find cupcake: " + e.getMessage());
        }

        int bottomId = getBottomIdByName(bottomName);
        int topId = getTopIdByName(toppingName);

        String insertSql = """
        INSERT INTO cupcake (cupcake_top_id, cupcake_bottom_id)
        VALUES (?, ?)
        RETURNING cupcake_id
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql)
        ) {
            preparedStatement.setInt(1, topId);
            preparedStatement.setInt(2, bottomId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("cupcake_id");
            }

        } catch (SQLException e) {
            System.out.println("Error in create cupcake: " + e.getMessage());
        }

        return -1;
    }


    private int getBottomIdByName(String bottomName) {
        String sql = """
        SELECT cupcake_bottom_id FROM "cupcakeBottom" WHERE bottom = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, bottomName);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("cupcake_bottom_id");
            }

        } catch (SQLException e) {
            System.out.println("Error in getBottomIdByName: " + e.getMessage());
        }

        return -1;
    }

    private int getTopIdByName(String toppingName) {
        String sql = """
        SELECT cupcake_top_id FROM "cupcakeTop" WHERE topping = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, toppingName);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getInt("cupcake_top_id");
            }

        } catch (SQLException e) {
            System.out.println("Error in getTopIdByName: " + e.getMessage());
        }

        return -1;
    }
}