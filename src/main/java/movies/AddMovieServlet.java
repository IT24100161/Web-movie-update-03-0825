package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/AddMovieServlet")
public class AddMovieServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")   // <-- matches META-INF/context.xml
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String ctx = request.getContextPath();

        boolean ok = false;

        if (title != null) {
            title = title.trim();

            //  Validation
            if (title.isEmpty()) {
                response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                        URLEncoder.encode("Title cannot be empty.", "UTF-8"));
                return;
            } else if (title.length() > 15) {
                response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                        URLEncoder.encode("Title cannot exceed 15 characters.", "UTF-8"));
                return;
            } else {
                MovieDAO dao = new MovieDAO(dataSource);
                ok = dao.addMovie(title);
            }
        }

        if (ok) {
            response.sendRedirect(ctx + "/addMovie.jsp?msg=" +
                    URLEncoder.encode("Movie added successfully!", "UTF-8"));
        } else if (title != null && !title.trim().isEmpty() && title.length() <= 15) {
            response.sendRedirect(ctx + "/addMovie.jsp?err=" +
                    URLEncoder.encode("Failed to add movie.", "UTF-8"));
        }
    }
}
