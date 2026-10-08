<%@page import="com.padmagriya.util.FormatUtil"%>
<%@page import="com.padmagriya.model.Pengguna"%>
<%@page import="com.padmagriya.model.Menu"%>
<%@page import="com.padmagriya.model.Pesanan"%>
<%@page import="com.padmagriya.model.ItemPesanan"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Pengguna pengguna = (Pengguna) session.getAttribute("pengguna");
    List<Menu> menuList = (List<Menu>) request.getAttribute("menuList");
    List<Pesanan> pesananList = (List<Pesanan>) request.getAttribute("pesananList");
    Map<String, List<Menu>> menuPerKategori = new LinkedHashMap<String, List<Menu>>();
    for (Menu item : menuList) {
        if (!menuPerKategori.containsKey(item.getKategori())) {
            menuPerKategori.put(item.getKategori(), new ArrayList<Menu>());
        }
        menuPerKategori.get(item.getKategori()).add(item);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pesanan - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <img class="brand-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
        <nav class="nav">
            <a href="<%= request.getContextPath() %>/dashboard"><span class="nav-code">D</span>Dashboard</a>
            <a class="active" href="<%= request.getContextPath() %>/pesanan"><span class="nav-code">P</span>Pesanan</a>
            <a href="<%= request.getContextPath() %>/pembayaran"><span class="nav-code">B</span>Pembayaran</a>
            <a href="<%= request.getContextPath() %>/logout"><span class="nav-code">O</span>Logout</a>
        </nav>
    </aside>
    <main class="content">
        <header class="topbar">
            <div>
                <h1>Sistem Pesanan</h1>
                <p>Buat pesanan dine-in, pilih menu per jenis, dan pantau status dapur.</p>
            </div>
            <div class="user-chip"><%= pengguna.getNama() %> | <%= pengguna.getRole() %></div>
        </header>
        <% if (request.getAttribute("pesan") != null) { %>
            <div class="message"><%= request.getAttribute("pesan") %></div>
        <% } %>
        <section class="card">
            <h2 class="section-title">Buat Pesanan</h2>
            <form method="post" action="<%= request.getContextPath() %>/pesanan">
                <div class="category-tabs">
                    <button class="category-filter active" type="button" data-category="all">Semua</button>
                    <% int tabIndex = 0; for (String kategori : menuPerKategori.keySet()) { %>
                        <button class="category-filter" type="button" data-category="kategori<%= tabIndex++ %>"><%= kategori %></button>
                    <% } %>
                </div>
                <% int kategoriIndex = 0; for (Map.Entry<String, List<Menu>> entry : menuPerKategori.entrySet()) { %>
                    <section id="kategori<%= kategoriIndex %>" class="menu-category" data-category="kategori<%= kategoriIndex++ %>">
                        <div class="section-heading">
                            <h3 class="section-title"><%= entry.getKey() %></h3>
                            <span class="subtle"><%= entry.getValue().size() %> menu</span>
                        </div>
                        <div class="menu-slider">
                            <% for (Menu item : entry.getValue()) { %>
                            <label class="menu-order-card">
                                <img src="<%= request.getContextPath() %>/<%= item.getFotoPath() %>" alt="<%= item.getNamaMenu() %>">
                                <span class="menu-order-body">
                                    <span class="menu-order-name"><%= item.getNamaMenu() %></span>
                                    <span class="subtle">Stok: <%= item.getStok() %></span>
                                    <span class="subtle"><%= FormatUtil.rupiah(item.getHarga()) %></span>
                                    <span class="order-controls">
                                        <input type="checkbox" name="idMenu" value="<%= item.getIdMenu() %>">
                                        <input type="number" name="jumlah_<%= item.getIdMenu() %>" min="0" value="0" aria-label="Jumlah <%= item.getNamaMenu() %>">
                                    </span>
                                </span>
                            </label>
                            <% } %>
                        </div>
                    </section>
                <% } %>
                <div class="form-action order-submit-action">
                    <button class="btn" type="submit">Simpan Pesanan</button>
                </div>
            </form>
        </section>
        <section class="card order-table-card">
            <h2 class="section-title">Daftar Pesanan</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Item</th>
                        <th>Total</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Pesanan pesanan : pesananList) { %>
                    <tr>
                        <td>#<%= pesanan.getIdPesanan() %><br><span class="subtle"><%= FormatUtil.tanggal(pesanan.getWaktuPesan()) %></span></td>
                        <td>
                            <% for (ItemPesanan item : pesanan.getDaftarItem()) { %>
                                <div class="ordered-line">
                                    <img src="<%= request.getContextPath() %>/<%= item.getMenu().getFotoPath() %>" alt="<%= item.getMenu().getNamaMenu() %>">
                                    <span><%= item.getMenu().getNamaMenu() %> x <%= item.getJumlah() %></span>
                                </div>
                            <% } %>
                        </td>
                        <td><%= FormatUtil.rupiah(pesanan.hitungTotal()) %></td>
                        <td><span class="badge dark"><%= pesanan.getStatus() %></span></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </main>
</div>
<script>
    document.querySelectorAll('.category-filter').forEach((button) => {
        button.addEventListener('click', () => {
            const selected = button.dataset.category;
            document.querySelectorAll('.category-filter').forEach((item) => item.classList.toggle('active', item === button));
            document.querySelectorAll('.menu-category').forEach((section) => {
                section.classList.toggle('hidden', selected !== 'all' && section.dataset.category !== selected);
            });
        });
    });
</script>
</body>
</html>
