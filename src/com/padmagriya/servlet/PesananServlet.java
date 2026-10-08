package com.padmagriya.servlet;

import com.padmagriya.dao.MenuDAO;
import com.padmagriya.dao.PesananDAO;
import com.padmagriya.model.ItemPesanan;
import com.padmagriya.model.Menu;
import com.padmagriya.model.Pesanan;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pesanan")
public class PesananServlet extends BaseServlet {
    private PesananDAO pesananDAO = new PesananDAO();
    private MenuDAO menuDAO = new MenuDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireKasir(request, response)) {
            return;
        }
        try {
            loadFlash(request);
            request.setAttribute("menuList", menuDAO.findAvailable());
            request.setAttribute("pesananList", pesananDAO.findAll());
            request.getRequestDispatcher("/pesanan.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireKasir(request, response)) {
            return;
        }
        try {
            Pesanan pesanan = new Pesanan(0);
            String[] idMenu = request.getParameterValues("idMenu");
            if (idMenu != null) {
                for (String id : idMenu) {
                    int jumlah = parseInt(request.getParameter("jumlah_" + id), 0);
                    if (jumlah > 0) {
                        Menu menu = menuDAO.findById(Integer.parseInt(id));
                        if (menu != null) {
                            pesanan.tambahItem(new ItemPesanan(menu, jumlah));
                        }
                    }
                }
            }
            pesananDAO.create(pesanan);
            setFlash(request, "Pesanan berhasil dibuat.");
            response.sendRedirect(request.getContextPath() + "/pesanan");
        } catch (SQLException ex) {
            setFlash(request, ex.getMessage());
            response.sendRedirect(request.getContextPath() + "/pesanan");
        }
    }

    private int parseInt(String value, int defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : Integer.parseInt(value);
    }
}
