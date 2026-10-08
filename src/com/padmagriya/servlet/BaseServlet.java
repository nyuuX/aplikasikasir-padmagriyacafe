package com.padmagriya.servlet;

import com.padmagriya.model.Pengguna;
import com.padmagriya.util.DatabaseInitializer;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class BaseServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        try {
            DatabaseInitializer.init();
        } catch (SQLException ex) {
            throw new ServletException("Gagal menyiapkan database: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        super.service(request, response);
    }

    protected boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (getUser(request) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    protected boolean requireRole(HttpServletRequest request, HttpServletResponse response, String role) throws IOException {
        Pengguna pengguna = getUser(request);
        if (pengguna == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        if (!role.equalsIgnoreCase(pengguna.getRole())) {
            setFlash(request, "Akses hanya untuk role " + role + ".");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }
        return true;
    }

    protected boolean requireManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Pengguna pengguna = getUser(request);
        if (pengguna == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        if (!isManager(pengguna)) {
            setFlash(request, "Akses hanya untuk admin atau owner.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return false;
        }
        return true;
    }

    protected boolean requireOwner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return requireRole(request, response, "OWNER");
    }

    protected boolean requireAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return requireRole(request, response, "ADMIN");
    }

    protected boolean requireKasir(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return requireRole(request, response, "KASIR");
    }

    protected boolean requireAnyRole(HttpServletRequest request, HttpServletResponse response, String... roles) throws IOException {
        Pengguna pengguna = getUser(request);
        if (pengguna == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        for (String role : roles) {
            if (role.equalsIgnoreCase(pengguna.getRole())) {
                return true;
            }
        }
        setFlash(request, "Akses tidak diizinkan untuk role " + pengguna.getRole() + ".");
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return false;
    }

    protected boolean isManager(Pengguna pengguna) {
        return pengguna != null && ("ADMIN".equalsIgnoreCase(pengguna.getRole()) || "OWNER".equalsIgnoreCase(pengguna.getRole()));
    }

    protected Pengguna getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (Pengguna) session.getAttribute("pengguna");
    }

    protected void loadFlash(HttpServletRequest request) {
        if (request.getAttribute("pesan") != null) {
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object pesan = session.getAttribute("pesan");
            if (pesan != null) {
                request.setAttribute("pesan", pesan);
                session.removeAttribute("pesan");
            }
        }
    }

    protected void setFlash(HttpServletRequest request, String pesan) {
        request.getSession().setAttribute("pesan", pesan);
    }
}
