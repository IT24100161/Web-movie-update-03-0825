package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/UpdateMovieServlet")
public class UpdateMovieServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ctx = request.getContextPath();
        String idParam = request.getParameter("id");
        String newTitle = request.getParameter("title");

        if (idParam == null || newTitle == null) {
            response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                    URLEncoder.encode("Invalid input for update.", "UTF-8"));
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            newTitle = newTitle.trim();

            // ✅ Validation (same rules as AddMovieServlet)
            if (newTitle.isEmpty()) {
                response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                        URLEncoder.encode("Title cannot be empty.", "UTF-8"));
                return;
            } else if (newTitle.length() > 20) {
                response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                        URLEncoder.encode("Title cannot exceed 15 characters.", "UTF-8"));
                return;
            }

            // Update in DB
            MovieDAO dao = new MovieDAO(dataSource);
            boolean ok = dao.updateMovie(id, newTitle);

            if (ok) {
                response.sendRedirect(ctx + "/addMovie.jsp?msg=" +
                        URLEncoder.encode("Movie updated successfully!", "UTF-8"));
            } else {
                response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                        URLEncoder.encode("Failed to update movie.", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                    URLEncoder.encode("Invalid movie ID.", "UTF-8"));
        }
    }
}
