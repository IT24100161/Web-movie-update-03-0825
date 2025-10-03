package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/AddUpcomingMovieServlet")
public class AddUpcomingMovieServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ctx = request.getContextPath();
        String title = request.getParameter("title");
        String releaseDate = request.getParameter("releaseDate");

        if (title == null || title.trim().isEmpty()) {
            response.sendRedirect(ctx + "/addUpcomingMovie.jsp?err=" +
                    URLEncoder.encode("Title cannot be empty.", "UTF-8"));
            return;
        }

        UpcomingMovieDAO dao = new UpcomingMovieDAO(dataSource);
        boolean ok = dao.addUpcomingMovie(title.trim(), releaseDate);

        if (ok) {
            response.sendRedirect(ctx + "/addUpcomingMovie.jsp?msg=" +
                    URLEncoder.encode("Upcoming movie added successfully!", "UTF-8"));
        } else {
            response.sendRedirect(ctx + "/addUpcomingMovie.jsp?err=" +
                    URLEncoder.encode("Failed to add upcoming movie.", "UTF-8"));
        }
    }
}
