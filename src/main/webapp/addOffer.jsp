<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.naming.InitialContext, javax.sql.DataSource" %>
<%@ page import="java.sql.*" %>
<%
    String msg = request.getParameter("msg");
    String err = request.getParameter("err");
    if (msg == null && request.getAttribute("msg") != null) msg = String.valueOf(request.getAttribute("msg"));
    if (err == null && request.getAttribute("err") != null) err = String.valueOf(request.getAttribute("err"));
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Offers (Admin)</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background: url('https://images.unsplash.com/photo-1747144293265-fc806b5dbc29?ixlib=rb-4.1.0&auto=format&fit=crop&w=1920&q=85')
            no-repeat center center fixed;
            background-size: cover;
            color: #fff;
            text-shadow: 1px 1px 4px rgba(0,0,0,0.7);
        }
        .glass-box {
            background: rgba(0,0,0,0.65);
            border-radius: 15px;
            padding: 25px;
            backdrop-filter: blur(8px);
            box-shadow: 0 8px 32px rgba(0,0,0,0.6);
        }
        .card, .table {
            background: rgba(255,255,255,0.9);
            color: #000;
        }
        .thead-dark th {
            background-color: #343a40 !important;
            color: #fff;
        }
    </style>
</head>
<body>

<div class="container py-4">
    <div class="glass-box mb-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="mb-0">Manage Offers</h2>
            <a href="<%= request.getContextPath() %>/adminPage.jsp"
               class="btn btn-outline-light btn-sm">← Back to Admin Panel</a>
        </div>

        <!-- Flash messages -->
        <% if (msg != null && !msg.isEmpty()) { %>
        <div class="alert alert-success"><%= msg %></div>
        <% } %>
        <% if (err != null && !err.isEmpty()) { %>
        <div class="alert alert-danger"><%= err %></div>
        <% } %>

        <!-- Add Offer Form -->
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/AddOfferServlet" method="post">
                    <div class="form-group">
                        <label for="title">Offer Title</label>
                        <input type="text" id="title" name="title" class="form-control"
                               placeholder="Enter offer title" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Offer</button>
                </form>
            </div>
        </div>

        <!-- Offers Table -->
        <h4 class="mb-3">Offers in Database</h4>
        <%
            try {
                DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MovieDB");
                String sql = "SELECT id, title FROM offers ORDER BY id DESC";
                try (Connection conn = ds.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead class="thead-dark">
                <tr>
                    <th style="width:90px;">ID</th>
                    <th>Title (Editable)</th>
                    <th style="width:200px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <%
                    boolean hasRows = false;
                    while (rs.next()) { hasRows = true; %>
                <tr>
                    <td><%= rs.getInt("id") %></td>
                    <td>
                        <!-- ✅ Inline Update Form -->
                        <form action="<%= request.getContextPath() %>/UpdateOfferServlet" method="post" class="form-inline">
                            <input type="hidden" name="id" value="<%= rs.getInt("id") %>">
                            <input type="text" name="title" value="<%= rs.getString("title") %>"
                                   class="form-control form-control-sm mr-2" required>
                            <button type="submit" class="btn btn-warning btn-sm">Update</button>
                        </form>
                    </td>
                    <td>
                        <!-- Delete button -->
                        <a href="<%= request.getContextPath() %>/DeleteOfferServlet?id=<%= rs.getInt("id") %>"
                           class="btn btn-danger btn-sm"
                           onclick="return confirm('Are you sure you want to delete this offer?');">
                            Delete
                        </a>
                    </td>
                </tr>
                <% } if (!hasRows) { %>
                <tr>
                    <td colspan="3" class="text-center text-muted">No offers added yet.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <%
            }
        } catch (Exception e) {
        %>
        <div class="alert alert-warning">Could not load offer list: <%= e.getMessage() %></div>
        <% } %>
    </div>
</div>

</body>
</html>
