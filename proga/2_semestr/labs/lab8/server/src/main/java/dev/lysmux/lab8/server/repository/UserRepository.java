package dev.lysmux.lab8.server.repository;

import dev.lysmux.lab8.server.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends Repository {
    public UserRepository(Connection connection) {
        super(connection);
    }

    public int add(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (login, hashed_password) VALUES (?, ?) RETURNING id");
            stmt.setString(1, user.login());
            stmt.setString(2, user.hashedPassword());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User get(String login) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id, login, hashed_password FROM users WHERE login = ?");
            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return null;

            return new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("hashed_password")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String login, String hashedPassword) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET hashed_password = ? WHERE login = ?");
            stmt.setString(1, hashedPassword);
            stmt.setString(2, login);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
