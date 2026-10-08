<%@page import="com.padmagriya.model.Pengguna"%>
<%@page import="com.padmagriya.model.StokBahan"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Pengguna pengguna = (Pengguna) session.getAttribute("pengguna");
    List<StokBahan> stokList = (List<StokBahan>) request.getAttribute("stokList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Stok - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <img class="brand-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
        <nav class="nav">
            <a href="<%= request.getContextPath() %>/dashboard"><span class="nav-code">D</span>Dashboard</a>
            <a href="<%= request.getContextPath() %>/menu"><span class="nav-code">M</span>Menu</a>
            <a class="active" href="<%= request.getContextPath() %>/stok"><span class="nav-code">S</span>Stok</a>
            <a href="<%= request.getContextPath() %>/pembayaran"><span class="nav-code">B</span>Riwayat Bayar</a>
            <a href="<%= request.getContextPath() %>/logout"><span class="nav-code">O</span>Logout</a>
        </nav>
    </aside>
    <main class="content">
        <header class="topbar">
            <div>
                <h1>Stok Bahan</h1>
                <p>Kontrol bahan baku dan batas minimum stok cafe.</p>
            </div>
            <div class="user-chip"><%= pengguna.getNama() %> | <%= pengguna.getRole() %></div>
        </header>
        <% if (request.getAttribute("pesan") != null) { %>
            <div class="message"><%= request.getAttribute("pesan") %></div>
        <% } %>
        <section class="card">
            <h2 class="section-title">Form Stok</h2>
            <form class="form-grid" method="post" action="<%= request.getContextPath() %>/stok">
                <input type="hidden" name="idBahan" value="">
                <label>Nama Bahan
                    <input type="text" name="namaBahan" required>
                </label>
                <label>Jumlah Stok
                    <input type="number" name="jumlahStok" min="0" step="0.1" required>
                </label>
                <label>Satuan
                    <input type="text" name="satuan" placeholder="kg, liter, pcs" required>
                </label>
                <label>Batas Minimum
                    <input type="number" name="batasMinimum" min="0" step="0.1" required>
                </label>
                <div class="full">
                    <button class="btn" type="submit">Simpan Stok</button>
                </div>
            </form>
        </section>
        <section class="card" style="margin-top:18px">
            <h2 class="section-title">Daftar Stok</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Bahan</th>
                        <th>Jumlah</th>
                        <th>Minimum</th>
                        <th>Status</th>
                        <th>Aksi</th>
                    </tr>
                </thead>
                <tbody>
                <% for (StokBahan stok : stokList) { %>
                    <tr>
                        <td><input form="stokForm<%= stok.getIdBahan() %>" type="text" name="namaBahan" value="<%= stok.getNamaBahan() %>" required></td>
                        <td>
                            <input form="stokForm<%= stok.getIdBahan() %>" type="number" name="jumlahStok" min="0" step="0.1" value="<%= stok.getJumlahStok() %>" required>
                            <input form="stokForm<%= stok.getIdBahan() %>" type="text" name="satuan" value="<%= stok.getSatuan() %>" required>
                        </td>
                        <td><input form="stokForm<%= stok.getIdBahan() %>" type="number" name="batasMinimum" min="0" step="0.1" value="<%= stok.getBatasMinimum() %>" required></td>
                        <td><span class="badge <%= stok.cekStokRendah() ? "red" : "green" %>"><%= stok.cekStokRendah() ? "Rendah" : "Aman" %></span></td>
                        <td>
                            <form id="stokForm<%= stok.getIdBahan() %>" class="inline-form" method="post" action="<%= request.getContextPath() %>/stok">
                                <input type="hidden" name="idBahan" value="<%= stok.getIdBahan() %>">
                                <button class="btn secondary" type="submit">Update</button>
                            </form>
                            <form class="inline-form" method="post" action="<%= request.getContextPath() %>/stok">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="idBahan" value="<%= stok.getIdBahan() %>">
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
