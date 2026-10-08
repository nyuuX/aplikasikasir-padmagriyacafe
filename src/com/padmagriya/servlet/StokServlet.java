package com.padmagriya.servlet;

import com.padmagriya.dao.StokBahanDAO;
import com.padmagriya.model.StokBahan;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/stok")
public class StokServlet extends BaseServlet {
    private StokBahanDAO stokBahanDAO = new StokBahanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireAdmin(request, response)) {
            return;
        }
        try {
            loadFlash(request);
            request.setAttribute("stokList", stokBahanDAO.findAll());
            request.getRequestDispatcher("/stok.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireAdmin(request, response)) {
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("delete".equals(action)) {
                stokBahanDAO.delete(Integer.parseInt(request.getParameter("idBahan")));
                setFlash(request, "Stok bahan berhasil dihapus.");
            } else {
                int idBahan = parseInt(request.getParameter("idBahan"), 0);
                StokBahan stok = new StokBahan(idBahan, request.getParameter("namaBahan"),
                        new BigDecimal(request.getParameter("jumlahStok")), request.getParameter("satuan"),
                        new BigDecimal(request.getParameter("batasMinimum")));
                if (!stok.validasiInput()) {
                    throw new SQLException("Input stok tidak valid.");
                }
                stokBahanDAO.save(stok);
                setFlash(request, "Stok bahan berhasil disimpan.");
            }
            response.sendRedirect(request.getContextPath() + "/stok");
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private int parseInt(String value, int defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : Integer.parseInt(value);
    }
}
