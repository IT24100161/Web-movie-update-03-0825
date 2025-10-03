package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet("/ViewUpcomingMoviesServlet")
public class ViewUpcomingMoviesServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UpcomingMovieDAO dao = new UpcomingMovieDAO(dataSource);
        List<UpcomingMovie> movies = dao.getAllUpcomingMovies();
        request.setAttribute("upcomingMovies", movies);

        request.getRequestDispatcher("/viewUpcomingMovies.jsp").forward(request, response);
    }
}
