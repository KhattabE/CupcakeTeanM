package app.persistence;

import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    private ConnectionPool connectionPool;

    public ProductMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // gets all cupcake bottoms
    public List<CupcakeBottom> getAllBottoms() {
        String sql = """
            SELECT * FROM "cupcakeBottom"
            """;

        List<CupcakeBottom> bottoms = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int cupcakeBottomId = rs.getInt("cupcake_bottom_id");
                String bottom = rs.getString("bottom");
                double price = rs.getDouble("price");

                CupcakeBottom cupcakeBottom = new CupcakeBottom(cupcakeBottomId, bottom, price);
                bottoms.add(cupcakeBottom);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return bottoms;
    }

    // gets all cupcake toppings
    public List<CupcakeTop> getAllToppings() {
        String sql = """
            SELECT * FROM "cupcakeTop"
            """;

        List<CupcakeTop> toppings = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int cupcakeTopId = rs.getInt("cupcake_top_id");
                String topping = rs.getString("topping");
                double price = rs.getDouble("price");

                CupcakeTop cupcakeTop = new CupcakeTop(cupcakeTopId, topping, price);
                toppings.add(cupcakeTop);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return toppings;
    }

    // gets one bottom by id
    public CupcakeBottom getBottomById(int cupcakeBottomId) {
        String sql = """
            SELECT * FROM "cupcakeBottom" WHERE cupcake_bottom_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, cupcakeBottomId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String bottom = rs.getString("bottom");
                double price = rs.getDouble("price");

                return new CupcakeBottom(cupcakeBottomId, bottom, price);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }

    // gets one topping by id
    public CupcakeTop getToppingById(int cupcakeTopId) {
        String sql = """
            SELECT * FROM "cupcakeTop" WHERE cupcake_top_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, cupcakeTopId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String topping = rs.getString("topping");
                double price = rs.getDouble("price");

                return new CupcakeTop(cupcakeTopId, topping, price);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }
}