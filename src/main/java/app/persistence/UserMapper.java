package app.persistence;

import app.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    private ConnectionPool connectionPool;

    public UserMapper(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // creates a new user in the database
    public void createUser(User user) {
        String sql = """
            INSERT INTO users (first_name, last_name, email, password_hash, balance, role) VALUES (?,?,?,?,?,?)
            """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPasswordHash());
            preparedStatement.setDouble(5, user.getBalance());
            preparedStatement.setString(6, user.getRole());

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }
    }

    // finds a user by email (used for login)
// finds a user by email (used for login)
    public User getUserByEmail(String email) {
        String sql = """
        SELECT * FROM users WHERE email = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String userEmail = rs.getString("email");
                String passwordHash = rs.getString("password_hash");
                double balance = rs.getDouble("balance");
                String role = rs.getString("role");

                return new User(id, firstName, lastName, userEmail, passwordHash, balance, role);
            }

        } catch (SQLException e) {
            System.out.println(sql);
        }

        return null;
    }

    // checks if login is valid (email + password)
    public User validateLogin(String email, String password) {
        String sql = """
        SELECT * FROM users WHERE email = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String passwordHash = rs.getString("password_hash");

                // simple check (later you can hash properly)
                if (passwordHash.equals(password)) {
                    int id = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String userEmail = rs.getString("email");
                    double balance = rs.getDouble("balance");
                    String role = rs.getString("role");

                    return new User(id, firstName, lastName, userEmail, passwordHash, balance, role);
                }
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }

    // gets a user by id (used for sessions/orders)
    public User getUserById(int userId) {
        String sql = """
        SELECT * FROM users WHERE user_id = ?
        """;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String passwordHash = rs.getString("password_hash");
                double balance = rs.getDouble("balance");
                String role = rs.getString("role");

                return new User(id, firstName, lastName, email, passwordHash, balance, role);
            }

        } catch (SQLException e){
            System.out.println("An error has has happend" + sql );
        }

        return null;
    }



}
