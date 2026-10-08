package com.padmagriya.dao;

import com.padmagriya.util.DatabaseUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LaporanDAO {
    public List<Map<String, Object>> transaksi() throws SQLException {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT p.id_pesanan, p.nomor_meja, p.total, p.status_pesanan, "
                + "pb.metode, pb.jumlah_bayar, pb.waktu_bayar FROM pesanan p "
                + "LEFT JOIN pembayaran pb ON p.id_pesanan=pb.id_pesanan ORDER BY p.waktu_pesan DESC");
        try {
            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("idPesanan", resultSet.getInt("id_pesanan"));
                row.put("nomorMeja", resultSet.getInt("nomor_meja"));
                row.put("total", resultSet.getBigDecimal("total"));
                row.put("statusPesanan", resultSet.getString("status_pesanan"));
                row.put("metode", resultSet.getString("metode"));
                row.put("jumlahBayar", resultSet.getBigDecimal("jumlah_bayar"));
                row.put("waktuBayar", resultSet.getTimestamp("waktu_bayar"));
                data.add(row);
            }
            return data;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public BigDecimal totalPenjualan() throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COALESCE(SUM(jumlah_bayar),0) FROM pembayaran");
        try {
            resultSet.next();
            return resultSet.getBigDecimal(1);
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public Map<String, Object> ringkasanKeuangan() throws SQLException {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        Connection connection = DatabaseUtil.getConnection();
        try {
            BigDecimal totalPenjualan = scalarDecimal(connection, "SELECT COALESCE(SUM(jumlah_bayar),0) FROM pembayaran");
            int totalTransaksi = scalarInt(connection, "SELECT COUNT(*) FROM pembayaran");
            BigDecimal qris = scalarDecimal(connection, "SELECT COALESCE(SUM(jumlah_bayar),0) FROM pembayaran WHERE metode='QRIS'");
            BigDecimal tunai = scalarDecimal(connection, "SELECT COALESCE(SUM(jumlah_bayar),0) FROM pembayaran WHERE metode='TUNAI'");
            BigDecimal rataRata = totalTransaksi == 0 ? BigDecimal.ZERO : totalPenjualan.divide(new BigDecimal(totalTransaksi), 0, RoundingMode.HALF_UP);
            BigDecimal estimasiModal = totalPenjualan.multiply(new BigDecimal("0.65"));
            BigDecimal estimasiLaba = totalPenjualan.subtract(estimasiModal);
            data.put("totalPenjualan", totalPenjualan);
            data.put("totalTransaksi", totalTransaksi);
            data.put("rataRataTransaksi", rataRata);
            data.put("penjualanQris", qris);
            data.put("penjualanTunai", tunai);
            data.put("estimasiModal", estimasiModal);
            data.put("estimasiLaba", estimasiLaba);
            data.put("marginLaba", "35%");
            return data;
        } finally {
            connection.close();
        }
    }

    private int scalarInt(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } finally {
            resultSet.close();
            statement.close();
        }
    }

    private BigDecimal scalarDecimal(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        try {
            resultSet.next();
            return resultSet.getBigDecimal(1);
        } finally {
            resultSet.close();
            statement.close();
        }
    }
}
