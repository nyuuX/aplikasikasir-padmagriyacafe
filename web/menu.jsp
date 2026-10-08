<%@page import="com.padmagriya.util.FormatUtil"%>
<%@page import="com.padmagriya.model.Pengguna"%>
<%@page import="com.padmagriya.model.Menu"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Pengguna pengguna = (Pengguna) session.getAttribute("pengguna");
    List<Menu> menuList = (List<Menu>) request.getAttribute("menuList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Menu - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <img class="brand-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
        <nav class="nav">
            <a href="<%= request.getContextPath() %>/dashboard"><span class="nav-code">D</span>Dashboard</a>
            <a class="active" href="<%= request.getContextPath() %>/menu"><span class="nav-code">M</span>Menu</a>
            <a href="<%= request.getContextPath() %>/stok"><span class="nav-code">S</span>Stok</a>
            <a href="<%= request.getContextPath() %>/pembayaran"><span class="nav-code">B</span>Riwayat Bayar</a>
            <a href="<%= request.getContextPath() %>/logout"><span class="nav-code">O</span>Logout</a>
        </nav>
    </aside>
    <main class="content">
        <header class="topbar">
            <div>
                <h1>Manajemen Menu</h1>
                <p>Tambah, ubah, hapus, dan atur ketersediaan menu cafe.</p>
            </div>
            <div class="user-chip"><%= pengguna.getNama() %> | <%= pengguna.getRole() %></div>
        </header>
        <% if (request.getAttribute("pesan") != null) { %>
            <div class="message"><%= request.getAttribute("pesan") %></div>
        <% } %>
        <section class="card">
            <h2 class="section-title">Form Menu</h2>
            <form class="form-grid" method="post" action="<%= request.getContextPath() %>/menu">
                <input type="hidden" name="idMenu" value="">
                <label>Nama Menu
                    <input type="text" name="namaMenu" required>
                </label>
                <label>Kategori
                    <select name="kategori">
                        <option>Makanan</option>
                        <option>Minuman</option>
                        <option>Snack</option>
                    </select>
                </label>
                <label>Harga
                    <input type="number" name="harga" min="0" step="500" required>
                </label>
                <label>Stok
                    <input type="number" name="stok" min="0" value="30" required>
                </label>
                <label class="checkbox-row">Tersedia
                    <input type="checkbox" name="tersedia" checked>
                </label>
                <div class="full">
                    <button class="btn" type="submit">Simpan Menu</button>
                </div>
            </form>
        </section>
        <section class="card menu-management">
            <h2 class="section-title">Daftar Menu</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Foto</th>
                        <th>Nama</th>
                        <th>Kategori</th>
                        <th>Harga</th>
                        <th>Stok</th>
                        <th>Status</th>
                        <th>Aksi</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Menu item : menuList) { %>
                    <tr>
                        <td><img class="menu-thumb" src="<%= request.getContextPath() %>/<%= item.getFotoPath() %>" alt="<%= item.getNamaMenu() %>"></td>
                        <td>
                            <input form="menuForm<%= item.getIdMenu() %>" type="text" name="namaMenu" value="<%= item.getNamaMenu() %>" required>
                        </td>
                        <td>
                            <select form="menuForm<%= item.getIdMenu() %>" name="kategori">
                                <option <%= "Makanan".equals(item.getKategori()) ? "selected" : "" %>>Makanan</option>
                                <option <%= "Minuman".equals(item.getKategori()) ? "selected" : "" %>>Minuman</option>
                                <option <%= "Snack".equals(item.getKategori()) ? "selected" : "" %>>Snack</option>
                            </select>
                        </td>
                        <td><input form="menuForm<%= item.getIdMenu() %>" type="number" name="harga" min="0" step="500" value="<%= item.getHarga().intValue() %>" required></td>
                        <td><input form="menuForm<%= item.getIdMenu() %>" type="number" name="stok" min="0" value="<%= item.getStok() %>" required></td>
                        <td>
                            <label class="checkbox-row">
                                <input form="menuForm<%= item.getIdMenu() %>" type="checkbox" name="tersedia" <%= item.isTersedia() ? "checked" : "" %>>
                                <span class="badge <%= item.isTersedia() ? "green" : "red" %>"><%= item.isTersedia() ? "Tersedia" : "Kosong" %></span>
                            </label>
                        </td>
                        <td>
                            <form id="menuForm<%= item.getIdMenu() %>" class="inline-form" method="post" action="<%= request.getContextPath() %>/menu">
                                <input type="hidden" name="idMenu" value="<%= item.getIdMenu() %>">
                                <button class="btn secondary" type="submit">Update</button>
                            </form>
                            <form class="inline-form" method="post" action="<%= request.getContextPath() %>/menu">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="idMenu" value="<%= item.getIdMenu() %>">
                                <button class="btn danger" type="submit">Hapus</button>
                            </form>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </main>
</div>
</body>
</html>
