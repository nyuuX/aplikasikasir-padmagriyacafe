package com.padmagriya.servlet;

import com.padmagriya.dao.MenuDAO;
import com.padmagriya.model.Menu;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/menu")
public class MenuServlet extends BaseServlet {
    private MenuDAO menuDAO = new MenuDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireAdmin(request, response)) {
            return;
        }
        try {
            loadFlash(request);
            request.setAttribute("menuList", menuDAO.findAll());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
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
                menuDAO.delete(Integer.parseInt(request.getParameter("idMenu")));
                setFlash(request, "Menu berhasil dihapus.");
            } else {
                int idMenu = parseInt(request.getParameter("idMenu"), 0);
                String nama = request.getParameter("namaMenu");
                String kategori = request.getParameter("kategori");
                BigDecimal harga = new BigDecimal(request.getParameter("harga"));
                boolean tersedia = request.getParameter("tersedia") != null;
                String fotoPath = request.getParameter("fotoPath");
                if (fotoPath == null || fotoPath.trim().isEmpty()) {
                    Menu existing = idMenu == 0 ? null : menuDAO.findById(idMenu);
                    fotoPath = existing == null ? "images/menu/menu-01.jpg" : existing.getFotoPath();
                }
                int stok = parseInt(request.getParameter("stok"), 0);
                menuDAO.save(new Menu(idMenu, nama, kategori, harga, tersedia, fotoPath, stok));
                setFlash(request, "Menu berhasil disimpan.");
            }
            response.sendRedirect(request.getContextPath() + "/menu");
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private int parseInt(String value, int defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : Integer.parseInt(value);
    }
}
