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
}