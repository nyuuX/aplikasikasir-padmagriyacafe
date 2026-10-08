package com.padmagriya.servlet;

import com.padmagriya.dao.PesananDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pembayaran")
public class PembayaranServlet extends BaseServlet {
    private PesananDAO pesananDAO = new PesananDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireAnyRole(request, response, "KASIR", "ADMIN")) {
            return;
        }
        try {
            loadFlash(request);
            if ("ADMIN".equalsIgnoreCase(getUser(request).getRole())) {
                request.setAttribute("pesananList", pesananDAO.findSudahBayar());
                request.setAttribute("readonlyPembayaran", Boolean.TRUE);
            } else {
                request.setAttribute("pesananList", pesananDAO.findBelumBayar());
                request.setAttribute("readonlyPembayaran", Boolean.FALSE);
            }
            request.getRequestDispatcher("/pembayaran.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireKasir(request, response)) {
            return;
        }
        int idPesanan = Integer.parseInt(request.getParameter("idPesanan"));
        String metode = request.getParameter("metode");
        try {
            if ("TUNAI".equals(metode)) {
                pesananDAO.bayarTunai(idPesanan, new BigDecimal(request.getParameter("uangDiterima")));
            } else {
                pesananDAO.bayarQris(idPesanan);
            }
            setFlash(request, "Pembayaran berhasil diproses.");
            response.sendRedirect(request.getContextPath() + "/pembayaran");
        } catch (SQLException ex) {
            request.setAttribute("pesan", ex.getMessage());
            doGet(request, response);
        }
    }
}
