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

    public List<Creature> getAllInDomain(String domain) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE domain = ?";
        List<Creature> results = new ArrayList<>();

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, domain);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new Creature(
                            rs.getInt("id"),
                            rs.getString("domain"),
                            rs.getString("kingdom"),
                            rs.getString("phylum"),
                            rs.getString("class_name"),
                            rs.getString("order"),
                            rs.getString("family"),
                            rs.getString("genus"),
                            rs.getString("species")
                        ));
                    }
                }
            }
            
        return results;
    }

    public void getAllKingdom(String kingdom) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE kingdom = ?";

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(2, kingdom);
            }
    }

    public void getAllPhylum(String phylum) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE phylum = ?";

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(3, phylum);
            }
    }

    public void getAllClass(String clazz) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE class_name = ?";

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(4, clazz);
            }
    }

    public void getAllOrder(String order) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE \"order\" = ?";

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(5, order);
            }
    }

    public void getAllFamily(String family) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE family = ?";

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(6, family);
            }
    }

    public void getAllGenus(String domain) throws SQLException {
        String sql = "SELECT * FROM creatures WHERE domain = ?";

        try (Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, domain);
            }
    }
    
}
