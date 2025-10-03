package movies;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/UpdateOfferServlet")
public class UpdateOfferServlet extends HttpServlet {

    @Resource(name = "jdbc/MovieDB")
    private DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ctx = request.getContextPath();
        String idParam = request.getParameter("id");
        String newTitle = request.getParameter("title");

        if (idParam == null || newTitle == null) {
            response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                    URLEncoder.encode("Invalid input for update.", "UTF-8"));
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            newTitle = newTitle.trim();

            // ✅ Validation
            if (newTitle.isEmpty()) {
                response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                        URLEncoder.encode("Offer title cannot be empty.", "UTF-8"));
                return;
            } else if (newTitle.length() > 15) {
                response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                        URLEncoder.encode("Offer title cannot exceed 15 characters.", "UTF-8"));
                return;
            }

            OfferDAO dao = new OfferDAO(dataSource);
            boolean ok = dao.updateOffer(id, newTitle);

            if (ok) {
                response.sendRedirect(ctx + "/addOffer.jsp?msg=" +
                        URLEncoder.encode("Offer updated successfully!", "UTF-8"));
            } else {
                response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                        URLEncoder.encode("Failed to update offer.", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                    URLEncoder.encode("Invalid offer ID.", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(ctx + "/addOffer.jsp?err=" +
                    URLEncoder.encode("Error: " + e.getMessage(), "UTF-8"));
        }
    }
}
