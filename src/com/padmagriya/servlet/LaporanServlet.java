package com.padmagriya.servlet;

import com.padmagriya.dao.LaporanDAO;
import com.padmagriya.service.SistemCafe;
import com.padmagriya.util.FormatUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/laporan")
public class LaporanServlet extends BaseServlet {
    private LaporanDAO laporanDAO = new LaporanDAO();
    private SistemCafe sistemCafe = new SistemCafe();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireOwner(request, response)) {
            return;
        }
        try {
            loadFlash(request);
            List<Map<String, Object>> transaksiList = laporanDAO.transaksi();
            Map<String, Object> ringkasanKeuangan = laporanDAO.ringkasanKeuangan();
            if ("pdf".equalsIgnoreCase(request.getParameter("format"))) {
                exportPdf(response, transaksiList, ringkasanKeuangan);
                return;
            }
            request.setAttribute("transaksiList", transaksiList);
            request.setAttribute("totalPenjualan", laporanDAO.totalPenjualan());
            request.setAttribute("ringkasanKeuangan", ringkasanKeuangan);
            request.setAttribute("ringkasanOop", sistemCafe.generateLaporan());
            request.getRequestDispatcher("/laporan.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void exportPdf(HttpServletResponse response, List<Map<String, Object>> transaksiList, Map<String, Object> ringkasan) throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("Laporan Keuangan Padmagriya Cafe");
        lines.add("Dicetak: " + FormatUtil.tanggal(LocalDateTime.now()));
        lines.add("");
        lines.add("Total Penjualan: " + FormatUtil.rupiah((BigDecimal) ringkasan.get("totalPenjualan")));
        lines.add("Total Transaksi: " + ringkasan.get("totalTransaksi"));
        lines.add("Rata-rata Transaksi: " + FormatUtil.rupiah((BigDecimal) ringkasan.get("rataRataTransaksi")));
        lines.add("Penjualan QRIS: " + FormatUtil.rupiah((BigDecimal) ringkasan.get("penjualanQris")));
        lines.add("Penjualan Tunai: " + FormatUtil.rupiah((BigDecimal) ringkasan.get("penjualanTunai")));
        lines.add("Estimasi Modal (65%): " + FormatUtil.rupiah((BigDecimal) ringkasan.get("estimasiModal")));
        lines.add("Estimasi Laba (35%): " + FormatUtil.rupiah((BigDecimal) ringkasan.get("estimasiLaba")));
        lines.add("");
        lines.add("Transaksi Terakhir");
        int count = 0;
        for (Map<String, Object> row : transaksiList) {
            if (count >= 12) {
                break;
            }
            Timestamp waktuBayar = (Timestamp) row.get("waktuBayar");
            String waktu = waktuBayar == null ? "-" : FormatUtil.tanggal(waktuBayar.toLocalDateTime());
            String bayar = row.get("jumlahBayar") == null ? "-" : FormatUtil.rupiah((BigDecimal) row.get("jumlahBayar"));
            lines.add("#" + row.get("idPesanan") + " | " + row.get("metode") + " | " + bayar + " | " + waktu);
            count++;
        }

        byte[] pdf = buildSimplePdf(lines);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=laporan-keuangan-padmagriya.pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    private byte[] buildSimplePdf(List<String> lines) throws IOException {
        StringBuilder content = new StringBuilder();
        int y = 800;
        for (int i = 0; i < lines.size(); i++) {
            int fontSize = i == 0 ? 18 : 11;
            content.append("BT /F1 ").append(fontSize).append(" Tf 48 ").append(y).append(" Td (")
                    .append(escapePdf(lines.get(i))).append(") Tj ET\n");
            y -= i == 0 ? 28 : 16;
        }
        byte[] contentBytes = content.toString().getBytes("ISO-8859-1");
        List<String> objects = new ArrayList<String>();
        objects.add("1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj\n");
        objects.add("2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj\n");
        objects.add("3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >> endobj\n");
        objects.add("4 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> endobj\n");
        objects.add("5 0 obj << /Length " + contentBytes.length + " >> stream\n" + content.toString() + "endstream endobj\n");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("%PDF-1.4\n".getBytes("ISO-8859-1"));
        List<Integer> offsets = new ArrayList<Integer>();
        for (String object : objects) {
            offsets.add(out.size());
            out.write(object.getBytes("ISO-8859-1"));
        }
        int xref = out.size();
        out.write(("xref\n0 " + (objects.size() + 1) + "\n").getBytes("ISO-8859-1"));
        out.write("0000000000 65535 f \n".getBytes("ISO-8859-1"));
        for (Integer offset : offsets) {
            out.write(String.format("%010d 00000 n \n", offset).getBytes("ISO-8859-1"));
        }
        out.write(("trailer << /Size " + (objects.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + xref + "\n%%EOF").getBytes("ISO-8859-1"));
        return out.toByteArray();
    }

    private String escapePdf(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }
}
