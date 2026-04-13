package app.persistence;

import app.entities.Cupcake;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CupcakeMapper {
    private ConnectionPool connectionPool;

    public CupcakeMapper(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    // creates a cupcake combination in the database
    public void createCupcake(Cupcake cupcake) {
        String sql = """
            INSERT INTO cupcake (cupcake_top_id, cupcake_bottom_id) VALUES (?,?)
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, cupcake.getCupcakeTopId());
            preparedStatement.setInt(2, cupcake.getCupcakeBottomId());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // finds a cupcake by id
    public Cupcake getCupcakeById(int cupcakeId) {
        String sql = """
            SELECT * FROM cupcake WHERE cupcake_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, cupcakeId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("cupcake_id");
                int cupcakeTopId = rs.getInt("cupcake_top_id");
                int cupcakeBottomId = rs.getInt("cupcake_bottom_id");

                return new Cupcake(id, cupcakeTopId, cupcakeBottomId);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }

    // finds a cupcake by topping id and bottom id
    public Cupcake getCupcakeByTopAndBottom(int cupcakeTopId, int cupcakeBottomId) {
        String sql = """
            SELECT * FROM cupcake WHERE cupcake_top_id = ? AND cupcake_bottom_id = ?
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, cupcakeTopId);
            preparedStatement.setInt(2, cupcakeBottomId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("cupcake_id");
                int topId = rs.getInt("cupcake_top_id");
                int bottomId = rs.getInt("cupcake_bottom_id");

                return new Cupcake(id, topId, bottomId);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
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
            System.out.println("Error in find cupcake: " + findSql);
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
            System.out.println("Error in create cupcake: " + insertSql);
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
            System.out.println("Error in getBottomIdByName: " + sql);
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
            System.out.println("Error in getTopIdByName: " + sql);
        }

        return -1;
    }








}