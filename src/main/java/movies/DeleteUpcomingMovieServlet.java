package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/DeleteUpcomingMovieServlet")
public class DeleteUpcomingMovieServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ctx = request.getContextPath();
        String idParam = request.getParameter("id");

        try {
            int id = Integer.parseInt(idParam);
            UpcomingMovieDAO dao = new UpcomingMovieDAO(dataSource);
            boolean ok = dao.deleteUpcomingMovie(id);

            if (ok) {
                response.sendRedirect(ctx + "/addUpcomingMovie.jsp?msg=" +
                        URLEncoder.encode("Upcoming movie deleted successfully!", "UTF-8"));
            } else {
                response.sendRedirect(ctx + "/addUpcomingMovie.jsp?err=" +
                        URLEncoder.encode("Failed to delete upcoming movie.", "UTF-8"));
            }
        } catch (Exception e) {
            response.sendRedirect(ctx + "/addUpcomingMovie.jsp?err=" +
                    URLEncoder.encode("Invalid movie id", "UTF-8"));
        }
    }
}
