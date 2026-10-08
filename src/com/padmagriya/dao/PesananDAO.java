package com.padmagriya.dao;

import com.padmagriya.model.ItemPesanan;
import com.padmagriya.model.Menu;
import com.padmagriya.model.PembayaranQRIS;
import com.padmagriya.model.PembayaranTunai;
import com.padmagriya.model.Pesanan;
import com.padmagriya.util.DatabaseUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PesananDAO {
    public List<Pesanan> findAll() throws SQLException {
        List<Pesanan> pesanan = new ArrayList<Pesanan>();
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM pesanan ORDER BY waktu_pesan DESC");
        try {
            while (resultSet.next()) {
                Pesanan item = mapPesanan(resultSet);
                loadItems(connection, item);
                pesanan.add(item);
            }
            return pesanan;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public List<Pesanan> findBelumBayar() throws SQLException {
        List<Pesanan> pesanan = new ArrayList<Pesanan>();
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pesanan WHERE status_bayar='BELUM BAYAR' ORDER BY waktu_pesan DESC");
        ResultSet resultSet = statement.executeQuery();
        try {
            while (resultSet.next()) {
                Pesanan item = mapPesanan(resultSet);
                loadItems(connection, item);
                pesanan.add(item);
            }
            return pesanan;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public List<Pesanan> findSudahBayar() throws SQLException {
        List<Pesanan> pesanan = new ArrayList<Pesanan>();
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pesanan WHERE status_bayar='LUNAS' ORDER BY waktu_pesan DESC");
        ResultSet resultSet = statement.executeQuery();
        try {
            while (resultSet.next()) {
                Pesanan item = mapPesanan(resultSet);
                loadItems(connection, item);
                pesanan.add(item);
            }
            return pesanan;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public Pesanan findById(int idPesanan) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pesanan WHERE id_pesanan=?");
        try {
            statement.setInt(1, idPesanan);
            ResultSet resultSet = statement.executeQuery();
            try {
                if (!resultSet.next()) {
                    return null;
                }
                Pesanan pesanan = mapPesanan(resultSet);
                loadItems(connection, pesanan);
                return pesanan;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
            connection.close();
        }
    }

    public void create(Pesanan pesanan) throws SQLException {
        if (!pesanan.validasiInput()) {
            throw new SQLException("Pesanan tidak valid. Pilih minimal satu item.");
        }
        Connection connection = DatabaseUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement orderStatement = connection.prepareStatement(
                    "INSERT INTO pesanan(waktu_pesan, nomor_meja, status_pesanan, total, status_bayar) VALUES(?,?,?,?, 'BELUM BAYAR')",
                    Statement.RETURN_GENERATED_KEYS);
            int idPesanan;
            try {
                orderStatement.setTimestamp(1, Timestamp.valueOf(pesanan.getWaktuPesan()));
                orderStatement.setInt(2, pesanan.getNomorMeja());
                orderStatement.setString(3, pesanan.getStatus());
                orderStatement.setBigDecimal(4, pesanan.hitungTotal());
                orderStatement.executeUpdate();
                ResultSet keys = orderStatement.getGeneratedKeys();
                try {
                    keys.next();
                    idPesanan = keys.getInt(1);
                } finally {
                    keys.close();
                }
            } finally {
                orderStatement.close();
            }
            PreparedStatement itemStatement = connection.prepareStatement(
                    "INSERT INTO item_pesanan(id_pesanan, id_menu, jumlah, subtotal) VALUES(?,?,?,?)");
            PreparedStatement stockStatement = connection.prepareStatement(
                    "UPDATE menu SET stok=stok-?, tersedia=CASE WHEN stok-? <= 0 THEN false ELSE tersedia END WHERE id_menu=? AND stok >= ?");
            try {
                for (ItemPesanan item : pesanan.getDaftarItem()) {
                    stockStatement.setInt(1, item.getJumlah());
                    stockStatement.setInt(2, item.getJumlah());
                    stockStatement.setInt(3, item.getMenu().getIdMenu());
                    stockStatement.setInt(4, item.getJumlah());
                    if (stockStatement.executeUpdate() == 0) {
                        throw new SQLException("Stok " + item.getMenu().getNamaMenu() + " tidak mencukupi.");
                    }
                    itemStatement.setInt(1, idPesanan);
                    itemStatement.setInt(2, item.getMenu().getIdMenu());
                    itemStatement.setInt(3, item.getJumlah());
                    itemStatement.setBigDecimal(4, item.getSubtotal());
                    itemStatement.addBatch();
                }
                itemStatement.executeBatch();
            } finally {
                stockStatement.close();
                itemStatement.close();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    public void updateStatus(int idPesanan, String statusPesanan) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE pesanan SET status_pesanan=? WHERE id_pesanan=?");
        try {
            statement.setString(1, statusPesanan);
            statement.setInt(2, idPesanan);
            statement.executeUpdate();
        } finally {
            statement.close();
            connection.close();
        }
    }

    public void bayarTunai(int idPesanan, BigDecimal uangDiterima) throws SQLException {
        Pesanan pesanan = findById(idPesanan);
        if (pesanan == null) {
            throw new SQLException("Pesanan tidak ditemukan.");
        }
        PembayaranTunai pembayaran = new PembayaranTunai(pesanan.hitungTotal(), uangDiterima);
        if (!pembayaran.prosesPembayaran()) {
            throw new SQLException("Uang diterima belum mencukupi total pembayaran.");
        }
        simpanPembayaran(idPesanan, "TUNAI", pembayaran.getJumlahBayar(), uangDiterima, pembayaran.getKembalian(), pembayaran.getStatusBayar(), null, pesanan.getNomorMeja());
    }

    public void bayarQris(int idPesanan) throws SQLException {
        Pesanan pesanan = findById(idPesanan);
        if (pesanan == null) {
            throw new SQLException("Pesanan tidak ditemukan.");
        }
        PembayaranQRIS pembayaran = new PembayaranQRIS(pesanan.hitungTotal());
        pembayaran.prosesPembayaran();
        simpanPembayaran(idPesanan, "QRIS", pembayaran.getJumlahBayar(), pembayaran.getJumlahBayar(), BigDecimal.ZERO,
                pembayaran.getStatusBayar(), pembayaran.getQrCode(), pesanan.getNomorMeja());
    }

    private void simpanPembayaran(int idPesanan, String metode, BigDecimal jumlahBayar, BigDecimal uangDiterima, BigDecimal kembalian, String statusBayar, String kodeQris, int nomorMeja) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO pembayaran(id_pesanan, metode, jumlah_bayar, uang_diterima, kembalian, status_bayar, waktu_bayar, kode_qris) VALUES(?,?,?,?,?,?,?,?)");
            try {
                statement.setInt(1, idPesanan);
                statement.setString(2, metode);
                statement.setBigDecimal(3, jumlahBayar);
                statement.setBigDecimal(4, uangDiterima);
                statement.setBigDecimal(5, kembalian);
                statement.setString(6, statusBayar);
                statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(8, kodeQris);
                statement.executeUpdate();
            } finally {
                statement.close();
            }
            PreparedStatement orderStatement = connection.prepareStatement("UPDATE pesanan SET status_bayar='LUNAS', status_pesanan='SELESAI' WHERE id_pesanan=?");
            try {
                orderStatement.setInt(1, idPesanan);
                orderStatement.executeUpdate();
            } finally {
                orderStatement.close();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    public Map<String, Object> ringkasan() throws SQLException {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        Connection connection = DatabaseUtil.getConnection();
        try {
            result.put("totalMenu", scalarInt(connection, "SELECT COUNT(*) FROM menu"));
            result.put("stokRendah", scalarInt(connection, "SELECT COUNT(*) FROM stok_bahan WHERE jumlah_stok <= batas_minimum"));
            result.put("omzetHariIni", scalarDecimal(connection, "SELECT COALESCE(SUM(jumlah_bayar),0) FROM pembayaran WHERE DATE(waktu_bayar)=CURDATE()"));
            return result;
        } finally {
            connection.close();
        }
    }

    public List<Map<String, Object>> omzetHarian(int jumlahHari) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT DATE(waktu_bayar) tanggal, COALESCE(SUM(jumlah_bayar),0) omzet "
                + "FROM pembayaran WHERE waktu_bayar >= DATE_SUB(CURDATE(), INTERVAL ? DAY) "
                + "GROUP BY DATE(waktu_bayar) ORDER BY tanggal");
        try {
            statement.setInt(1, jumlahHari - 1);
            ResultSet resultSet = statement.executeQuery();
            try {
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("tanggal", resultSet.getDate("tanggal").toString());
                    row.put("omzet", resultSet.getBigDecimal("omzet"));
                    data.add(row);
                }
                return data;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
            connection.close();
        }
    }

    public List<Map<String, Object>> menuSeringDipesan(int limit) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT m.nama_menu, m.kategori, m.foto_path, COALESCE(SUM(ip.jumlah),0) jumlah, COALESCE(SUM(ip.subtotal),0) omzet "
                + "FROM item_pesanan ip JOIN menu m ON ip.id_menu=m.id_menu "
                + "GROUP BY m.id_menu, m.nama_menu, m.kategori, m.foto_path ORDER BY jumlah DESC, omzet DESC LIMIT ?");
        try {
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            try {
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("namaMenu", resultSet.getString("nama_menu"));
                    row.put("kategori", resultSet.getString("kategori"));
                    row.put("fotoPath", resultSet.getString("foto_path"));
                    row.put("jumlah", resultSet.getInt("jumlah"));
                    row.put("omzet", resultSet.getBigDecimal("omzet"));
                    data.add(row);
                }
                return data;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
            connection.close();
        }
    }

    public List<Map<String, Object>> menuFavoritPerKategori(int limitPerKategori) throws SQLException {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT nama_menu, kategori, foto_path, jumlah, omzet FROM ("
                + "SELECT m.nama_menu, m.kategori, m.foto_path, COALESCE(SUM(ip.jumlah),0) jumlah, COALESCE(SUM(ip.subtotal),0) omzet, "
                + "ROW_NUMBER() OVER (PARTITION BY m.kategori ORDER BY COALESCE(SUM(ip.jumlah),0) DESC, COALESCE(SUM(ip.subtotal),0) DESC) peringkat "
                + "FROM item_pesanan ip JOIN menu m ON ip.id_menu=m.id_menu "
                + "GROUP BY m.id_menu, m.nama_menu, m.kategori, m.foto_path"
                + ") ranked WHERE peringkat <= ? ORDER BY kategori, peringkat");
        try {
            statement.setInt(1, limitPerKategori);
            ResultSet resultSet = statement.executeQuery();
            try {
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("namaMenu", resultSet.getString("nama_menu"));
                    row.put("kategori", resultSet.getString("kategori"));
                    row.put("fotoPath", resultSet.getString("foto_path"));
                    row.put("jumlah", resultSet.getInt("jumlah"));
                    row.put("omzet", resultSet.getBigDecimal("omzet"));
                    data.add(row);
                }
                return data;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
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

    private void loadItems(Connection connection, Pesanan pesanan) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT ip.id_item, ip.jumlah, m.* FROM item_pesanan ip JOIN menu m ON ip.id_menu=m.id_menu WHERE ip.id_pesanan=?");
        try {
            statement.setInt(1, pesanan.getIdPesanan());
            ResultSet resultSet = statement.executeQuery();
            try {
                while (resultSet.next()) {
                    Menu menu = new Menu(resultSet.getInt("id_menu"), resultSet.getString("nama_menu"),
                            resultSet.getString("kategori"), resultSet.getBigDecimal("harga"), resultSet.getBoolean("tersedia"),
                            resultSet.getString("foto_path"), resultSet.getInt("stok"));
                    pesanan.tambahItem(new ItemPesanan(resultSet.getInt("id_item"), menu, resultSet.getInt("jumlah")));
                }
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    private void updateMeja(Connection connection, int nomorMeja, String status) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE meja SET status_meja=? WHERE nomor_meja=?");
        try {
            statement.setString(1, status);
            statement.setInt(2, nomorMeja);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    private Pesanan mapPesanan(ResultSet resultSet) throws SQLException {
        Timestamp waktu = resultSet.getTimestamp("waktu_pesan");
        return new Pesanan(resultSet.getInt("id_pesanan"), resultSet.getInt("nomor_meja"),
                waktu == null ? null : waktu.toLocalDateTime(), resultSet.getString("status_pesanan"));
    }
}
