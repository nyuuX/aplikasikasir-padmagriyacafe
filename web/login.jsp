<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <main class="login-page">
        <section class="login-hero">
            <img class="login-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
            <h1>Padmagriya Cafe</h1>
            <p>Sistem operasional cafe untuk pesanan, pembayaran, stok, menu, dan laporan keuangan.</p>
        </section>
        <section class="login-panel">
            <form class="login-box" method="post" action="<%= request.getContextPath() %>/login">
                <h2 class="section-title">Masuk Sistem</h2>
                <% if (request.getAttribute("pesan") != null) { %>
                    <div class="message"><%= request.getAttribute("pesan") %></div>
                <% } %>
                <div class="grid">
                    <label>Username
                        <input type="text" name="username" required autofocus>
                    </label>
                    <label>Password
                        <input type="password" name="password" required>
                    </label>
                    <button class="btn" type="submit">Login</button>
                    <p class="subtle">Akun awal: admin/admin123, kasir/kasir123, owner/owner123</p>
                </div>
            </form>
        </section>
    </main>
</body>
</html>
