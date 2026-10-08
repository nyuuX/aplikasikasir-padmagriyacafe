package com.padmagriya.servlet;

import com.padmagriya.dao.PenggunaDAO;
import com.padmagriya.model.Pengguna;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private PenggunaDAO penggunaDAO = new PenggunaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loadFlash(request);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Pengguna pengguna = penggunaDAO.login(username, password);
            if (pengguna == null) {
                request.setAttribute("pesan", "Username atau password salah.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            request.getSession().setAttribute("pengguna", pengguna);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
