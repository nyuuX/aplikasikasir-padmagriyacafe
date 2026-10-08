<%@page import="com.padmagriya.model.StokBahan"%>
<%@page import="com.padmagriya.util.FormatUtil"%>
<%@page import="com.padmagriya.model.Pengguna"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Pengguna pengguna = (Pengguna) session.getAttribute("pengguna");
    boolean admin = "ADMIN".equalsIgnoreCase(pengguna.getRole());
    boolean owner = "OWNER".equalsIgnoreCase(pengguna.getRole());
    boolean kasir = "KASIR".equalsIgnoreCase(pengguna.getRole());
    Map ringkasan = (Map) request.getAttribute("ringkasan");
    List<Map<String, Object>> menuSeringDipesan = (List<Map<String, Object>>) request.getAttribute("menuSeringDipesan");
    List<StokBahan> stokRendahList = (List<StokBahan>) request.getAttribute("stokRendahList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <img class="brand-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
        <nav class="nav">
            <a class="active" href="<%= request.getContextPath() %>/dashboard"><span class="nav-code">D</span>Dashboard</a>
            <% if (admin) { %>
                <a href="<%= request.getContextPath() %>/menu"><span class="nav-code">M</span>Menu</a>
                <a href="<%= request.getContextPath() %>/stok"><span class="nav-code">S</span>Stok</a>
                <a href="<%= request.getContextPath() %>/pembayaran"><span class="nav-code">B</span>Riwayat Bayar</a>
            <% } %>
            <% if (kasir) { %>
                <a href="<%= request.getContextPath() %>/pesanan"><span class="nav-code">P</span>Pesanan</a>
                <a href="<%= request.getContextPath() %>/pembayaran"><span class="nav-code">B</span>Pembayaran</a>
            <% } %>
            <% if (owner) { %>
                <a href="<%= request.getContextPath() %>/laporan"><span class="nav-code">L</span>Laporan</a>
            <% } %>
            <a href="<%= request.getContextPath() %>/logout"><span class="nav-code">O</span>Logout</a>
        </nav>
    </aside>
    <main class="content">
        <header class="topbar">
            <div>
                <h1>Dashboard</h1>
                <p>Ringkasan operasional Padmagriya Cafe hari ini.</p>
            </div>
            <div class="user-chip"><%= pengguna.getNama() %> | <%= pengguna.getRole() %></div>
        </header>
        <% if (request.getAttribute("pesan") != null) { %>
            <div class="message"><%= request.getAttribute("pesan") %></div>
        <% } %>
        <% if (admin && stokRendahList != null && !stokRendahList.isEmpty()) { %>
            <section class="stock-alert">
                <strong>Notifikasi stok rendah</strong>
                <div>
                    <% for (StokBahan stok : stokRendahList) { %>
                        <span class="badge red"><%= stok.getNamaBahan() %>: <%= stok.getJumlahStok() %> <%= stok.getSatuan() %></span>
                    <% } %>
                </div>
            </section>
        <% } %>
        <section class="grid grid-3">
            <article class="card stat">
                <span>Total Menu</span>
                <strong><%= ringkasan.get("totalMenu") %></strong>
            </article>
            <% if (admin) { %>
                <article class="card stat">
                    <span>Stok Rendah</span>
                    <strong><%= ringkasan.get("stokRendah") %></strong>
                </article>
            <% } %>
            <article class="card stat">
                <span>Omzet Hari Ini</span>
                <strong><%= FormatUtil.rupiah((BigDecimal) ringkasan.get("omzetHariIni")) %></strong>
            </article>
        </section>
        <section class="dashboard-panels">
            <article class="card">
                <div class="section-heading">
                    <h2 class="section-title">Menu Favorit per Kategori</h2>
                    <span class="subtle">2 teratas berdasarkan jumlah dipesan</span>
                </div>
                <div class="popular-list">
                    <% if (menuSeringDipesan != null && !menuSeringDipesan.isEmpty()) {
                        for (Map<String, Object> menu : menuSeringDipesan) {
                            String foto = menu.get("fotoPath") == null ? "images/menu/menu-01.jpg" : String.valueOf(menu.get("fotoPath"));
                    %>
                        <div class="popular-item">
                            <img src="<%= request.getContextPath() %>/<%= foto %>" alt="<%= menu.get("namaMenu") %>">
                            <div>
                                <strong><%= menu.get("namaMenu") %></strong>
                                <span><%= menu.get("kategori") %> | <%= menu.get("jumlah") %> terjual</span>
                            </div>
                            <b><%= FormatUtil.rupiah((BigDecimal) menu.get("omzet")) %></b>
                        </div>
                    <%  }
                    } else { %>
                        <p class="subtle">Belum ada item pesanan yang bisa dihitung.</p>
                    <% } %>
                </div>
            </article>
        </section>
    </main>
</div>
</body>
</html>
