package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import models.Creature;
import models.CreatureImage;

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
            rs.getString("order_name"),
            rs.getString("family"),
            rs.getString("genus"),
            rs.getString("species")
        );
    }

    public int addEntry(String domain, String kingdom, String phylum, String clazz, String order, String family, String genus, String species, String common_name, String description) throws SQLException {
        String sql = """
                INSERT INTO creatures
                (domain, kingdom, phylum, class_name, order_name, family, genus, species, common_name, description)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
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
                    ps.setString(9, common_name);
                    ps.setString(10, description);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return rs.getInt(1);
                    }
                }
                throw new SQLException("Failed to insert creature");
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

    public List <String> getDistinctValuesInRank(String rank) throws SQLException {
        String sql = "SELECT DISTINCT " + rank + " FROM creatures";
        List <String> results = new ArrayList<>();

        try(Connection conn = Queries.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

                while(rs.next()) {
                    results.add(rs.getString(1));
                }
            }
        return results;
    }

    public List<String> getDistinctValuesFiltered(String nextRank, String[] rankArray, String[] selectedValues, int currentRank) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT " + nextRank + " FROM creatures WHERE 1=1"
        );

        for (int i = 0; i <= currentRank; i++) {
            if (selectedValues[i] != null) {
                sql.append(" AND ").append(rankArray[i]).append(" = ?");
            }
        }

        List<String> results = new ArrayList<>();

        try (Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            for (int i = 0; i <= currentRank; i++) {
                if (selectedValues[i] != null) {
                    ps.setString(paramIndex++, selectedValues[i]);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(rs.getString(1));
                }
            }
        }

        return results;
    }

    public void addCreatureImage(int creatureId, String imagePath, String caption, boolean isStandard, String source, String attribution, Integer sortOrder) throws SQLException {

        String sql = """
            INSERT INTO creature_images
            (creature_id, image_path, caption, sort_order)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, creatureId);
            ps.setString(2, imagePath);
            ps.setString(3, caption);
            ps.setInt(4, sortOrder);

            ps.executeUpdate();
        }
    }

    public List<CreatureImage> getImagesForCreature(int creatureId) throws SQLException {
        String sql = "SELECT * FROM creature_images WHERE creature_id = ? ORDER BY sort_order";

        List<CreatureImage> results = new ArrayList<>();

        try (Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, creatureId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new CreatureImage(rs.getInt("id"), rs.getInt("creature_id"), rs.getString("image_path"), rs.getString("caption"), rs.getBoolean("is_standard"), rs.getString("source"), rs.getString("attribution"), rs.getObject("sort_order", Integer.class)));

                }
            }
        }
        return results;
    }

    public void upsertStandardImage(int creatureId, String imageUrl, String source, String attribution) throws SQLException {
        String sql = "INSERT INTO creature_standard_image (creature_id, image_url, source, attribution) VALUES (?, ?, ?, ?) ON CONFLICT (creature_id) DO UPDATE SET image_url = EXCLUDED.image_url, source = EXCLUDED.source, attribution = EXCLUDED.attribution";
        
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, creatureId); ps.setString(2, imageUrl); ps.setString(3, source); ps.setString(4, attribution);
            ps.executeUpdate();
        }
    }

    public CreatureImage getStandardImage(int creatureId) throws SQLException {
        String sql = "SELECT * FROM creature_standard_image WHERE creature_id = ?";
        
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, creatureId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new CreatureImage(rs.getInt("id"), rs.getInt("creature_id"), rs.getString("image_url"), null, true, rs.getString("source"), rs.getString("attribution"), null);
            }
        }
        return null;
    }

    public void addUserImage(int creatureId, String imagePath, String uploadedBy) throws SQLException {
        String sql = "INSERT INTO creature_user_images (creature_id, image_path, uploaded_by) VALUES (?, ?, ?)";
        
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, creatureId); ps.setString(2, imagePath); ps.setString(3, uploadedBy);
            ps.executeUpdate();
        }
    }

    public List<CreatureImage> getUserImages(int creatureId) throws SQLException {
        String sql = "SELECT * FROM creature_user_images WHERE creature_id = ? ORDER BY uploaded_at";
        List<CreatureImage> results = new ArrayList<>();
        
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, creatureId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(new CreatureImage(rs.getInt("id"), rs.getInt("creature_id"), rs.getString("image_url"), null, true, rs.getString("source"), rs.getString("attribution"), null));
            }
        }
        return results;
    }
}
