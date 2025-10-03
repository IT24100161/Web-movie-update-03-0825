
package movies;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "PaymentDetailsServlet", urlPatterns = {"/PaymentDetailsServlet"})
public class PaymentDetailsServlet extends HttpServlet {
    private DataSource ds;

    @Override
    public void init() throws ServletException {
        try {
            ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MovieDB");
        } catch (NamingException e) {
            throw new ServletException("JNDI DataSource lookup failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String bookingIdStr = req.getParameter("bookingId");
        String payerName = req.getParameter("payer_name");
        String phone = req.getParameter("phone");
        String method = req.getParameter("method");

        // ✅ Check for missing fields
        if (payerName == null || payerName.isEmpty()
                || phone == null || phone.isEmpty()
                || method == null || method.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customerPayment.jsp?error=" +
                    java.net.URLEncoder.encode("All fields are required", "UTF-8") +
                    (bookingIdStr != null ? "&bookingId=" + bookingIdStr : ""));
            return;
        }

        // ✅ Phone number validation: must be exactly 10 digits
        if (!phone.matches("\\d{10}")) {
            resp.sendRedirect(req.getContextPath() + "/customerPayment.jsp?error=" +
                    java.net.URLEncoder.encode("Please enter a valid 10-digit phone number", "UTF-8") +
                    (bookingIdStr != null ? "&bookingId=" + bookingIdStr : ""));
            return;
        }

        // ✅ Booking ID validation
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customerPayment.jsp?error=" +
                    java.net.URLEncoder.encode("Booking not confirmed yet", "UTF-8"));
            return;
        }

        long bookingId = Long.parseLong(bookingIdStr);

        // ✅ Save payment details
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO payments(booking_id, payer_name, phone, method) VALUES (?,?,?,?)")) {
                ps.setLong(1, bookingId);
                ps.setString(2, payerName);
                ps.setString(3, phone);
                ps.setString(4, method);
                ps.executeUpdate();
            }
            // Redirect to success page
            resp.sendRedirect(req.getContextPath() + "/paymentSuccess.jsp?bookingId=" + bookingId);
        } catch (SQLException e) {
            e.printStackTrace(); // log error
            resp.sendRedirect(req.getContextPath() + "/customerPayment.jsp?error=" +
                    java.net.URLEncoder.encode("Database error: " + e.getMessage(), "UTF-8") +
                    "&bookingId=" + bookingIdStr);
        }
    }
}
