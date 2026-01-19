package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Creature;

import io.github.cdimascio.dotenv.Dotenv;

public class Queries {
private static final Dotenv dotenv = Dotenv.configure()
                                            .filename("dot.env")
                                            .load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private Creature mapCreature(ResultSet rs) throws SQLException {
        return new Creature(
            rs.getInt("id"),
            rs.getString("domain"),
            rs.getString("kingdom"),
            rs.getString("phylum"),
            rs.getString("class_name"),
            rs.getString("order"),
            rs.getString("family"),
            rs.getString("genus"),
            rs.getString("species")
        );
    }

    public void addEntry(String domain, String kingdom, String phylum, String clazz, String order, String family, String genus, String species) throws SQLException {
        String sql = """
                INSERT INTO creatures
                (domain, kingdom, phylum, class_name, "order", family, genus, species)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

                try (Connection conn = Queries.connect();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setString(1, domain);
                    ps.setString(2, kingdom);
                    ps.setString(3, phylum);
                    ps.setString(4, clazz);
                    ps.setString(5, order);
                    ps.setString(6, family);
                    ps.setString(7, genus);
                    ps.setString(8, species);

                    ps.executeUpdate();
                }
    }

    public List<Creature> getAllCreatures() throws SQLException {
        String sql = "SELECT * FROM creatures";
        List<Creature> results = new ArrayList<>();

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapCreature(rs));
                    }
                }
            }

        return results;
    }

    public List<Creature> getAllInSpecifics(String rank, String name) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE " + rank + " = ?";

        List<Creature> results = new ArrayList<>();

        try(Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);

                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        results.add(mapCreature(rs));
                    }
                }
            }
        
        return results;
    }
}
