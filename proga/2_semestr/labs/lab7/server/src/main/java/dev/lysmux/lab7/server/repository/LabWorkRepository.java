package dev.lysmux.lab7.server.repository;

import dev.lysmux.lab7.common.collection.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabWorkRepository extends Repository {
    public LabWorkRepository(Connection connection) {
        super(connection);
    }

    public int add(LabWork labWork) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO lab_works_full (" +
                            "lw_owner_id, " +
                            "lw_name, " +
                            "lw_minimal_point, " +
                            "lw_difficulty, " +
                            "coord_x, coord_y, " +
                            "author_name, author_birthday, author_weight, " +
                            "loc_name, loc_x, loc_y" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING lw_id"
            );
            stmt.setInt(1, labWork.getOwnerId());
            stmt.setString(2, labWork.getName());
            stmt.setLong(3, labWork.getMinimalPoint());
            stmt.setString(4, labWork.getDifficulty().name());
            stmt.setInt(5, labWork.getCoordinates().getX());
            stmt.setDouble(6, labWork.getCoordinates().getY());
            stmt.setString(7, labWork.getAuthor().getName());
            stmt.setDate(8, new Date(labWork.getAuthor().getBirthday().getTime()));
            stmt.setLong(9, labWork.getAuthor().getWeight());
            stmt.setString(10, labWork.getAuthor().getLocation().getName());
            stmt.setInt(11, labWork.getAuthor().getLocation().getX());
            stmt.setFloat(12, labWork.getAuthor().getLocation().getY());

            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("lw_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LabWork> getAll() {
        List<LabWork> labWorks = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT " +
                            "lw_id, " +
                            "lw_owner_id, " +
                            "lw_name, " +
                            "lw_minimal_point, " +
                            "lw_difficulty, " +
                            "lw_created_at, " +
                            "coord_x, " +
                            "coord_y, " +
                            "author_name, " +
                            "author_birthday, " +
                            "author_weight, " +
                            "loc_name, " +
                            "loc_x, " +
                            "loc_y " +
                            "FROM lab_works_full");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LabWork labWork = new LabWork(
                        rs.getInt("lw_id"),
                        rs.getInt("lw_owner_id"),
                        rs.getString("lw_name"),
                        rs.getLong("lw_minimal_point"),
                        Difficulty.valueOf(rs.getString("lw_difficulty").toUpperCase()),
                        rs.getDate("lw_created_at").toLocalDate(),
                        new Coordinates(
                                rs.getInt("coord_x"),
                                rs.getDouble("coord_y")
                        ),
                        new Person(
                                rs.getString("author_name"),
                                rs.getDate("author_birthday"),
                                rs.getLong("author_weight"),
                                new Location(
                                        rs.getInt("loc_x"),
                                        rs.getFloat("loc_y"),
                                        rs.getString("loc_name")
                                )
                        )
                );
                labWorks.add(labWork);
            }

            return labWorks;

        } catch (SQLException e) {
            return null;
        }
    }

    public void remove(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM lab_works WHERE id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByOwnerID(int ownerID) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM lab_works WHERE owner_id=?");
            stmt.setInt(1, ownerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
