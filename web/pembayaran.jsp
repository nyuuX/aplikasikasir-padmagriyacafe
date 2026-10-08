<%@page import="com.padmagriya.util.FormatUtil"%>
<%@page import="com.padmagriya.model.Pengguna"%>
<%@page import="com.padmagriya.model.Pesanan"%>
<%@page import="com.padmagriya.model.ItemPesanan"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Pengguna pengguna = (Pengguna) session.getAttribute("pengguna");
    boolean admin = "ADMIN".equalsIgnoreCase(pengguna.getRole());
    boolean readonlyPembayaran = Boolean.TRUE.equals(request.getAttribute("readonlyPembayaran"));
    List<Pesanan> pesananList = (List<Pesanan>) request.getAttribute("pesananList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pembayaran - Padmagriya Cafe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <img class="brand-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
        <nav class="nav">
            <a href="<%= request.getContextPath() %>/dashboard"><span class="nav-code">D</span>Dashboard</a>
            <% if (admin) { %>
                <a href="<%= request.getContextPath() %>/menu"><span class="nav-code">M</span>Menu</a>
                <a href="<%= request.getContextPath() %>/stok"><span class="nav-code">S</span>Stok</a>
            <% } %>
            <% if (!admin) { %>
                <a href="<%= request.getContextPath() %>/pesanan"><span class="nav-code">P</span>Pesanan</a>
            <% } %>
            <a class="active" href="<%= request.getContextPath() %>/pembayaran"><span class="nav-code">B</span><%= admin ? "Riwayat Bayar" : "Pembayaran" %></a>
            <a href="<%= request.getContextPath() %>/logout"><span class="nav-code">O</span>Logout</a>
        </nav>
    </aside>
    <main class="content">
        <header class="topbar">
            <div>
                <h1><%= readonlyPembayaran ? "Riwayat Pembayaran" : "Pembayaran" %></h1>
                <p><%= readonlyPembayaran ? "Track record transaksi pembayaran cafe." : "Proses tunai atau QRIS dengan receipt otomatis." %></p>
            </div>
            <div class="user-chip"><%= pengguna.getNama() %> | <%= pengguna.getRole() %></div>
        </header>
        <% if (request.getAttribute("pesan") != null) { %>
            <div class="message"><%= request.getAttribute("pesan") %></div>
        <% } %>
        <section class="grid grid-2">
            <% for (Pesanan pesanan : pesananList) {
                String qrisCode = "QRIS-PADMAGRIYA-" + pesanan.hitungTotal().toPlainString().replace(".", "") + "-" + pesanan.getIdPesanan();
            %>
            <article class="card payment-card">
                <div class="section-heading">
                    <h2 class="section-title">Pesanan #<%= pesanan.getIdPesanan() %></h2>
                    <span class="badge dark"><%= pesanan.getStatus() %></span>
                </div>
                <p class="subtle"><%= FormatUtil.tanggal(pesanan.getWaktuPesan()) %></p>
                <div class="receipt-lines">
                    <% for (ItemPesanan item : pesanan.getDaftarItem()) { %>
                        <div><span><%= item.getMenu().getNamaMenu() %> x <%= item.getJumlah() %></span><strong><%= FormatUtil.rupiah(item.getSubtotal()) %></strong></div>
                    <% } %>
                </div>
                <h3 class="payment-total">Total: <%= FormatUtil.rupiah(pesanan.hitungTotal()) %></h3>
                <% if (!readonlyPembayaran) { %>
                <form class="form-grid payment-form" method="post" action="<%= request.getContextPath() %>/pembayaran" data-total="<%= pesanan.hitungTotal().intValue() %>">
                    <input type="hidden" name="idPesanan" value="<%= pesanan.getIdPesanan() %>">
                    <label>Metode
                        <select name="metode" class="payment-method">
                            <option>TUNAI</option>
                            <option>QRIS</option>
                        </select>
                    </label>
                    <label>Uang Diterima
                        <input class="cash-input" type="number" name="uangDiterima" min="0" step="500" value="<%= pesanan.hitungTotal().intValue() %>">
                    </label>
                    <div class="full qris-receipt">
                        <div class="receipt-paper">
                            <img class="receipt-logo" src="<%= request.getContextPath() %>/images/logo-padmagriya.svg" alt="Padmagriya Cafe">
                            <div class="receipt-title">Receipt QRIS</div>
                            <div class="qris-box" aria-label="<%= qrisCode %>">
                                <span></span><span></span><span></span><span></span>
                            </div>
                            <p class="qris-code"><%= qrisCode %></p>
                            <div class="receipt-lines">
                                <div><span>Pesanan</span><strong>#<%= pesanan.getIdPesanan() %></strong></div>
                                <div><span>Nominal scan</span><strong><%= FormatUtil.rupiah(pesanan.hitungTotal()) %></strong></div>
                            </div>
                        </div>
                    </div>
                    <div class="full">
                        <button class="btn" type="submit">Proses Pembayaran</button>
                    </div>
                </form>
                <% } else { %>
                    <p><span class="badge green">Sudah dibayar</span></p>
                <% } %>
            </article>
            <% } %>
            <% if (pesananList.isEmpty()) { %>
                <article class="card"><p><%= readonlyPembayaran ? "Belum ada track record pembayaran." : "Tidak ada pesanan yang menunggu pembayaran." %></p></article>
            <% } %>
        </section>
    </main>
</div>
<script>
    document.querySelectorAll('.payment-form').forEach((form) => {
        const method = form.querySelector('.payment-method');
        const cashInput = form.querySelector('.cash-input');
        const receipt = form.querySelector('.qris-receipt');
        const total = form.dataset.total;
        function syncPayment() {
            const isQris = method.value === 'QRIS';
            receipt.style.display = isQris ? 'block' : 'none';
            if (isQris) {
                cashInput.value = total;
                cashInput.readOnly = true;
            } else {
                cashInput.readOnly = false;
            }
        }
        method.addEventListener('change', syncPayment);
        syncPayment();
    });
</script>
</body>
</html>
