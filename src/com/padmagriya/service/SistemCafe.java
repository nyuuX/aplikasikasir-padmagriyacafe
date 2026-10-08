package com.padmagriya.service;

import com.padmagriya.dao.LaporanDAO;
import com.padmagriya.dao.MenuDAO;
import com.padmagriya.dao.PesananDAO;
import com.padmagriya.dao.StokBahanDAO;
import com.padmagriya.model.Menu;
import com.padmagriya.model.Pesanan;
import com.padmagriya.model.Reportable;
import com.padmagriya.model.StokBahan;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SistemCafe implements Reportable {
    private MenuDAO menuDAO;
    private PesananDAO pesananDAO;
    private StokBahanDAO stokBahanDAO;
    private LaporanDAO laporanDAO;

    public SistemCafe() {
        this.menuDAO = new MenuDAO();
        this.pesananDAO = new PesananDAO();
        this.stokBahanDAO = new StokBahanDAO();
        this.laporanDAO = new LaporanDAO();
    }

    public void tambahMenu(Menu menu) throws SQLException {
        menuDAO.save(menu);
    }

    public void buatPesanan(Pesanan pesanan) throws SQLException {
        pesananDAO.create(pesanan);
    }

    public List<Menu> getDaftarMenu() throws SQLException {
        return menuDAO.findAll();
    }

    public List<Pesanan> getDaftarPesanan() throws SQLException {
        return pesananDAO.findAll();
    }

    public List<StokBahan> cekStok() throws SQLException {
        return stokBahanDAO.findAll();
    }

    public Map<String, Object> ringkasanOperasional() throws SQLException {
        return pesananDAO.ringkasan();
    }

    @Override
    public String generateLaporan() {
        try {
            return "Total transaksi: " + laporanDAO.transaksi().size() + ", total penjualan: " + laporanDAO.totalPenjualan();
        } catch (SQLException ex) {
            return "Laporan gagal dibuat: " + ex.getMessage();
        }
    }
}
