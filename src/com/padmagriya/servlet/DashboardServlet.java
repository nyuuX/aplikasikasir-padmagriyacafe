package com.padmagriya.servlet;

import com.padmagriya.dao.PesananDAO;
import com.padmagriya.dao.StokBahanDAO;
import com.padmagriya.service.SistemCafe;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet extends BaseServlet {
    private SistemCafe sistemCafe = new SistemCafe();
    private PesananDAO pesananDAO = new PesananDAO();
    private StokBahanDAO stokBahanDAO = new StokBahanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireLogin(request, response)) {
            return;
        }
        try {
            loadFlash(request);
            request.setAttribute("ringkasan", sistemCafe.ringkasanOperasional());
            request.setAttribute("menuSeringDipesan", pesananDAO.menuFavoritPerKategori(2));
            request.setAttribute("stokRendahList", stokBahanDAO.findLowStock());
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
