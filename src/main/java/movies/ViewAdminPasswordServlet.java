package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;

@WebServlet("/ViewAdminPasswordServlet")
public class ViewAdminPasswordServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect(request.getContextPath() + "/adminLogin.jsp?error=" +
                    URLEncoder.encode("Please login first", "UTF-8"));
            return;
        }

        String username = String.valueOf(session.getAttribute("adminUser"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT password FROM admin WHERE username = ?")) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String pw = rs.getString("password");
                    request.setAttribute("password", pw);
                }
            }

            // forward to JSP to display
            request.getRequestDispatcher("/viewAdminPassword.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/changeAdminPassword.jsp?err=" +
                    URLEncoder.encode("Database error: " + e.getMessage(), "UTF-8"));
        }
    }
}
