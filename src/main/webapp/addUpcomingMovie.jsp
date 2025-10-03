<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.naming.InitialContext, javax.sql.DataSource" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Upcoming Movies</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <style>
        body {
            background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)),
            url('https://images.unsplash.com/photo-1747144293265-fc806b5dbc29?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D')
            no-repeat center center fixed;
            background-size: cover;
            font-family: 'Roboto', sans-serif;
            color: #fff;
        }
        .panel-box {
            background: rgba(0,0,0,0.75);
            border-radius: 18px;
            padding: 30px;
            margin: 60px auto;
            max-width: 800px;
            text-align: center;
            backdrop-filter: blur(8px);
            box-shadow: 0 8px 32px rgba(0,0,0,0.6);
        }
        .btn { margin: 5px; border-radius: 30px; }
        .table { background: #fff; color: #000; border-radius: 10px; }
    </style>
</head>
<body>

<div class="panel-box">
    <h2>🎬 Add Upcoming Movies</h2>
    <hr style="border-top: 2px solid rgba(255,255,255,0.4);">

    <!-- Add Form -->
    <form action="AddUpcomingMovieServlet" method="post" class="mb-4">
        <div class="form-row justify-content-center">
            <div class="col-md-6 mb-2">
                <input type="text" name="title" class="form-control" placeholder="Movie Title" required>
            </div>
            <div class="col-md-4 mb-2">
                <input type="date" name="releaseDate" class="form-control">
            </div>
            <div class="col-md-2 mb-2">
                <button type="submit" class="btn btn-success btn-block">Add</button>
            </div>
        </div>
    </form>

    <!-- List Upcoming Movies -->
    <h4 class="mb-3">Upcoming Movies List</h4>
    <%
        try {
            DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MovieDB");
            String sql = "SELECT id, title, release_date FROM upcoming_movies ORDER BY id DESC";
            try (Connection conn = ds.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
    %>
    <table class="table table-bordered table-striped">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Release Date</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            boolean has = false;
            while (rs.next()) { has = true; %>
        <tr>
            <td><%= rs.getInt("id") %></td>
            <td><%= rs.getString("title") %></td>
            <td><%= rs.getDate("release_date") %></td>
            <td>
                <a href="DeleteUpcomingMovieServlet?id=<%= rs.getInt("id") %>"
                   class="btn btn-danger btn-sm"
                   onclick="return confirm('Delete this upcoming movie?');">Delete</a>
            </td>
        </tr>
        <% } if (!has) { %>
        <tr><td colspan="4" class="text-center">No upcoming movies yet.</td></tr>
        <% } %>
        </tbody>
    </table>
    <%
        }
    } catch (Exception e) {
    %>
    <div class="alert alert-danger">Error: <%= e.getMessage() %></div>
    <% } %>

    <a href="adminPage.jsp" class="btn btn-secondary mt-3">← Back to Admin Panel</a>
</div>

</body>
</html>
