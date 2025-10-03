<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String adminUser = (String) session.getAttribute("adminUser");
    if (adminUser == null) {
        response.sendRedirect(request.getContextPath() + "/adminLogin.jsp?error=Please+login");
        return;
    }

    String password = (String) request.getAttribute("password");
%>
<!DOCTYPE html>
<html>
<head>
    <title>View Admin Password</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <style>
        html, body {
            height: 100%;
            margin: 0;
            font-family: 'Roboto', sans-serif;
            color: white;
            text-shadow: 1px 1px 4px rgba(0,0,0,0.7);
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
            min-height: 100%;
            overflow-x: hidden;
            position: relative;
            background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)),
            url('https://images.unsplash.com/photo-1747144293265-fc806b5dbc29?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            background-attachment: fixed;
        }

        .form-box {
            background: rgba(0,0,0,0.65);
            padding: 35px;
            border-radius: 18px;
            -webkit-backdrop-filter: blur(8px);
            backdrop-filter: blur(8px);
            box-shadow: 0 8px 32px rgba(0,0,0,0.6);
            width: 100%;
            max-width: 450px;
        }

        .btn {
            font-weight: bold;
            padding: 10px 24px;
            border-radius: 40px;
            transition: all 0.3s ease;
        }
        .btn-secondary:hover { transform: scale(1.05); box-shadow: 0 4px 14px rgba(200,200,200,0.5); }

        hr { border-top: 2px solid rgba(255,255,255,0.6); }
    </style>
</head>
<body>

<div class="form-box">
    <h2>🔐 View Admin Password</h2>
    <hr>

    <% if (password != null) { %>
    <div class="alert alert-info">
        Current Password: <strong><%= password %></strong>
    </div>
    <% } else { %>
    <div class="alert alert-warning">⚠ No password found.</div>
    <% } %>

    <button type="button" class="btn btn-secondary mt-3"
            onclick="window.location.href='changeAdminPassword.jsp'">← Back</button>
</div>

</body>
</html>
