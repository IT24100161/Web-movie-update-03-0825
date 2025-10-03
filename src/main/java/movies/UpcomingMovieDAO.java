package movies;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpcomingMovieDAO {
    private final DataSource ds;

    public UpcomingMovieDAO(DataSource ds) {
        this.ds = ds;
    }

    public boolean addUpcomingMovie(String title, String releaseDate) {
        String sql = "INSERT INTO upcoming_movies (title, release_date) VALUES (?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, releaseDate);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUpcomingMovie(int id) {
        String sql = "DELETE FROM upcoming_movies WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UpcomingMovie> getAllUpcomingMovies() {
        List<UpcomingMovie> list = new ArrayList<>();
        String sql = "SELECT id, title, release_date FROM upcoming_movies ORDER BY release_date ASC";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new UpcomingMovie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("release_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
