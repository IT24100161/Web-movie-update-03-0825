package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/AddOfferServlet")
public class AddOfferServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")   // from META-INF/context.xml
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String ctx = request.getContextPath();

        // Validate empty
        if (title == null || (title = title.trim()).isEmpty()) {
            response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                    URLEncoder.encode("Offer title cannot be empty", "UTF-8"));
            return;
        }

        // Validate max length
        if (title.length() > 20) {
            response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                    URLEncoder.encode("Offer title cannot exceed 20 characters", "UTF-8"));
            return;
        }

        // OPTIONAL: You can also enforce a minimum length (e.g., at least 3 characters)
        // if (title.length() < 3) {
        //     response.sendRedirect(ctx + "/addOffer.jsp?err=" +
        //             URLEncoder.encode("Offer title must be at least 3 characters long", "UTF-8"));
        //     return;
        // }

        OfferDAO dao = new OfferDAO(dataSource);
        try {
            dao.addOffer(title);
            response.sendRedirect(ctx + "/addOffer.jsp?msg=" +
                    URLEncoder.encode("Offer added successfully", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                    URLEncoder.encode("Error: " + e.getMessage(), "UTF-8"));
        }
    }
}
