<%@page import="com.padmagriya.util.FormatUtil"%>
<%@page import="com.padmagriya.model.Pengguna"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Pengguna pengguna = (Pengguna) session.getAttribute("pengguna");
    List<Map<String, Object>> transaksiList = (List<Map<String, Object>>) request.getAttribute("transaksiList");
    Map<String, Object> ringkasanKeuangan = (Map<String, Object>) request.getAttribute("ringkasanKeuangan");
    BigDecimal totalPenjualan = (BigDecimal) request.getAttribute("totalPenjualan");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Laporan - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <img class="brand-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
        <nav class="nav">
            <a href="<%= request.getContextPath() %>/dashboard"><span class="nav-code">D</span>Dashboard</a>
            <a class="active" href="<%= request.getContextPath() %>/laporan"><span class="nav-code">L</span>Laporan</a>
            <a href="<%= request.getContextPath() %>/logout"><span class="nav-code">O</span>Logout</a>
        </nav>
    </aside>
    <main class="content">
        <header class="topbar">
            <div>
                <h1>Laporan Keuangan</h1>
                <p>Riwayat pembayaran, rangkuman pendapatan, dan estimasi laba cafe.</p>
            </div>
            <div class="topbar-actions">
                <a class="btn secondary" href="<%= request.getContextPath() %>/laporan?format=pdf">Export PDF</a>
                <div class="user-chip"><%= pengguna.getNama() %> | <%= pengguna.getRole() %></div>
            </div>
        </header>
        <section class="grid grid-4">
            <article class="card stat">
                <span>Total Penjualan</span>
                <strong><%= FormatUtil.rupiah(totalPenjualan) %></strong>
            </article>
            <article class="card stat">
                <span>Estimasi Laba</span>
                <strong><%= FormatUtil.rupiah((BigDecimal) ringkasanKeuangan.get("estimasiLaba")) %></strong>
            </article>
            <article class="card stat">
                <span>Total Transaksi</span>
                <strong><%= ringkasanKeuangan.get("totalTransaksi") %></strong>
            </article>
            <article class="card stat">
                <span>Rata-rata</span>
                <strong><%= FormatUtil.rupiah((BigDecimal) ringkasanKeuangan.get("rataRataTransaksi")) %></strong>
            </article>
        </section>
        <section class="grid grid-2 dashboard-panels">
            <article class="card">
                <h2 class="section-title">Metode Pembayaran</h2>
                <div class="summary-line"><span>QRIS</span><strong><%= FormatUtil.rupiah((BigDecimal) ringkasanKeuangan.get("penjualanQris")) %></strong></div>
                <div class="summary-line"><span>Tunai</span><strong><%= FormatUtil.rupiah((BigDecimal) ringkasanKeuangan.get("penjualanTunai")) %></strong></div>
            </article>
            <article class="card">
                <h2 class="section-title">Rangkuman Laba</h2>
                <div class="summary-line"><span>Estimasi Modal</span><strong><%= FormatUtil.rupiah((BigDecimal) ringkasanKeuangan.get("estimasiModal")) %></strong></div>
                <div class="summary-line"><span>Margin</span><strong><%= ringkasanKeuangan.get("marginLaba") %></strong></div>
                <p class="subtle">Estimasi laba memakai asumsi margin 35% karena data HPP detail belum tersedia.</p>
            </article>
        </section>
        <section class="card order-table-card">
            <h2 class="section-title">Riwayat Transaksi</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Pesanan</th>
                        <th>Total</th>
                        <th>Metode</th>
                        <th>Bayar</th>
                        <th>Waktu Bayar</th>
                    </tr>
                </thead>
                <tbody>
                <% for (Map<String, Object> row : transaksiList) {
                    Timestamp waktuBayar = (Timestamp) row.get("waktuBayar");
                %>
                    <tr>
                        <td>#<%= row.get("idPesanan") %><br><span class="badge dark"><%= row.get("statusPesanan") %></span></td>
                        <td><%= FormatUtil.rupiah((BigDecimal) row.get("total")) %></td>
                        <td><%= row.get("metode") == null ? "-" : row.get("metode") %></td>
                        <td><%= row.get("jumlahBayar") == null ? "-" : FormatUtil.rupiah((BigDecimal) row.get("jumlahBayar")) %></td>
                        <td><%= waktuBayar == null ? "-" : FormatUtil.tanggal(waktuBayar.toLocalDateTime()) %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </main>
</div>
</body>
</html>
