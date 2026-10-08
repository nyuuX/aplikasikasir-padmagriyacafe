package com.padmagriya.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static boolean initialized;

    public static synchronized void init() throws SQLException {
        if (initialized) {
            return;
        }
        Connection connection = DatabaseUtil.getConnection();
        try {
            createTables(connection);
            seedData(connection);
            initialized = true;
        } finally {
            connection.close();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pengguna ("
                    + "id_pengguna INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nama VARCHAR(100) NOT NULL,"
                    + "username VARCHAR(50) NOT NULL UNIQUE,"
                    + "password VARCHAR(100) NOT NULL,"
                    + "role VARCHAR(20) NOT NULL,"
                    + "level_akses VARCHAR(30),"
                    + "id_kasir VARCHAR(30)"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS menu ("
                    + "id_menu INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nama_menu VARCHAR(100) NOT NULL,"
                    + "kategori VARCHAR(50) NOT NULL,"
                    + "harga DECIMAL(12,2) NOT NULL,"
                    + "tersedia BOOLEAN NOT NULL DEFAULT TRUE,"
                    + "foto_path VARCHAR(180),"
                    + "stok INT NOT NULL DEFAULT 30"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS meja ("
                    + "id_meja INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nomor_meja INT NOT NULL UNIQUE,"
                    + "status_meja VARCHAR(20) NOT NULL DEFAULT 'KOSONG'"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS stok_bahan ("
                    + "id_bahan INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nama_bahan VARCHAR(100) NOT NULL,"
                    + "jumlah_stok DECIMAL(12,2) NOT NULL,"
                    + "satuan VARCHAR(20) NOT NULL,"
                    + "batas_minimum DECIMAL(12,2) NOT NULL"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pesanan ("
                    + "id_pesanan INT AUTO_INCREMENT PRIMARY KEY,"
                    + "waktu_pesan DATETIME NOT NULL,"
                    + "nomor_meja INT NOT NULL,"
                    + "status_pesanan VARCHAR(20) NOT NULL,"
                    + "total DECIMAL(12,2) NOT NULL DEFAULT 0,"
                    + "status_bayar VARCHAR(20) NOT NULL DEFAULT 'BELUM BAYAR'"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_pesanan ("
                    + "id_item INT AUTO_INCREMENT PRIMARY KEY,"
                    + "id_pesanan INT NOT NULL,"
                    + "id_menu INT NOT NULL,"
                    + "jumlah INT NOT NULL,"
                    + "subtotal DECIMAL(12,2) NOT NULL,"
                    + "FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan) ON DELETE CASCADE,"
                    + "FOREIGN KEY (id_menu) REFERENCES menu(id_menu)"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS pembayaran ("
                    + "id_pembayaran INT AUTO_INCREMENT PRIMARY KEY,"
                    + "id_pesanan INT NOT NULL,"
                    + "metode VARCHAR(20) NOT NULL,"
                    + "jumlah_bayar DECIMAL(12,2) NOT NULL,"
                    + "uang_diterima DECIMAL(12,2),"
                    + "kembalian DECIMAL(12,2),"
                    + "status_bayar VARCHAR(20) NOT NULL,"
                    + "waktu_bayar DATETIME NOT NULL,"
                    + "kode_qris VARCHAR(100),"
                    + "FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan) ON DELETE CASCADE"
                    + ")");
        } finally {
            statement.close();
        }
        ensureColumn(connection, "menu", "foto_path", "ALTER TABLE menu ADD COLUMN foto_path VARCHAR(180)");
        ensureColumn(connection, "menu", "stok", "ALTER TABLE menu ADD COLUMN stok INT NOT NULL DEFAULT 30");
    }

    private static void seedData(Connection connection) throws SQLException {
        if (!userExists(connection, "admin")) {
            insertUser(connection, "Admin Padmagriya", "admin", "admin123", "ADMIN", "PENUH", null);
        }
        if (!userExists(connection, "kasir")) {
            insertUser(connection, "Kasir Padmagriya", "kasir", "kasir123", "KASIR", null, "KSR-001");
        }
        if (!userExists(connection, "owner")) {
            insertUser(connection, "Owner Padmagriya", "owner", "owner123", "OWNER", "OWNER", null);
        }
        seedMenus(connection);
        limitMenusPerCategory(connection, 10);
        if (isTableEmpty(connection, "meja")) {
            for (int i = 1; i <= 10; i++) {
                insertMeja(connection, i);
            }
        }
        if (isTableEmpty(connection, "stok_bahan")) {
            insertStok(connection, "Biji kopi", 8, "kg", 2);
            insertStok(connection, "Susu segar", 12, "liter", 3);
            insertStok(connection, "Beras", 20, "kg", 5);
            insertStok(connection, "Ayam", 10, "kg", 3);
            insertStok(connection, "Gula aren", 1, "kg", 2);
        }
    }

    private static void seedMenus(Connection connection) throws SQLException {
        String[][] menus = {
            {"Nasi Ayam Sambal Matah", "Makanan", "32000", "images/menu/menu-21.jpg"},
            {"Rice Bowl Beef Teriyaki", "Makanan", "39000", "images/menu/menu-22.jpg"},
            {"Chicken Katsu Curry", "Makanan", "36000", "images/menu/menu-23.jpg"},
            {"Pasta Creamy Mushroom", "Makanan", "38000", "images/menu/menu-24.jpg"},
            {"Spaghetti Bolognese", "Makanan", "37000", "images/menu/menu-25.jpg"},
            {"Nasi Goreng Kampung", "Makanan", "30000", "images/menu/menu-26.jpg"},
            {"Mie Goreng Seafood", "Makanan", "34000", "images/menu/menu-27.jpg"},
            {"Dori Sambal Dabu", "Makanan", "38000", "images/menu/menu-28.jpg"},
            {"Chicken Mentai Rice", "Makanan", "37000", "images/menu/menu-29.jpg"},
            {"Beef Blackpepper Rice", "Makanan", "41000", "images/menu/menu-30.jpg"},
            {"Kopi Susu Padmagriya", "Minuman", "18000", "images/menu/menu-01.jpg"},
            {"Americano Dingin", "Minuman", "17000", "images/menu/menu-02.jpg"},
            {"Cappuccino Classic", "Minuman", "23000", "images/menu/menu-03.jpg"},
            {"Cafe Latte", "Minuman", "24000", "images/menu/menu-04.jpg"},
            {"Caramel Macchiato", "Minuman", "28000", "images/menu/menu-05.jpg"},
            {"Mocha Hazelnut", "Minuman", "29000", "images/menu/menu-06.jpg"},
            {"Espresso Double", "Minuman", "16000", "images/menu/menu-07.jpg"},
            {"Cold Brew Vanilla", "Minuman", "26000", "images/menu/menu-08.jpg"},
            {"Matcha Latte", "Minuman", "22000", "images/menu/menu-09.jpg"},
            {"Chocolate Signature", "Minuman", "24000", "images/menu/menu-10.jpg"},
            {"Croissant Butter", "Snack", "20000", "images/menu/menu-41.png"},
            {"French Fries Truffle", "Snack", "24000", "images/menu/menu-42.png"},
            {"Onion Rings", "Snack", "21000", "images/menu/menu-43.png"},
            {"Chicken Wings BBQ", "Snack", "32000", "images/menu/menu-44.png"},
            {"Nachos Cheese", "Snack", "28000", "images/menu/menu-45.png"},
            {"Pisang Goreng Karamel", "Snack", "22000", "images/menu/menu-46.png"},
            {"Roti Bakar Cokelat Keju", "Snack", "23000", "images/menu/menu-47.png"},
            {"Churros Cinnamon", "Snack", "24000", "images/menu/menu-48.png"},
            {"Waffle Maple", "Snack", "27000", "images/menu/menu-49.png"},
            {"Brownies Fudge", "Snack", "26000", "images/menu/menu-52.png"}
        };
        for (int i = 0; i < menus.length; i++) {
            if (!menuExists(connection, menus[i][0])) {
                insertMenu(connection, menus[i][0], menus[i][1], Integer.parseInt(menus[i][2]), menus[i][3]);
            }
        }
    }

    private static void limitMenusPerCategory(Connection connection, int limit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE m FROM menu m "
                + "LEFT JOIN item_pesanan ip ON ip.id_menu=m.id_menu "
                + "JOIN ("
                + "SELECT id_menu FROM ("
                + "SELECT id_menu, ROW_NUMBER() OVER (PARTITION BY kategori ORDER BY id_menu ASC) peringkat FROM menu"
                + ") ranked WHERE peringkat > ?"
                + ") extra ON extra.id_menu=m.id_menu "
                + "WHERE ip.id_menu IS NULL");
        try {
            statement.setInt(1, limit);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    private static boolean isTableEmpty(Connection connection, String table) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + table);
        try {
            resultSet.next();
            return resultSet.getInt(1) == 0;
        } finally {
            resultSet.close();
            statement.close();
        }
    }

    private static boolean userExists(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM pengguna WHERE username=?");
        try {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            try {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    private static boolean menuExists(Connection connection, String namaMenu) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM menu WHERE nama_menu=?");
        try {
            statement.setString(1, namaMenu);
            ResultSet resultSet = statement.executeQuery();
            try {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
        }
    }

    private static void ensureColumn(Connection connection, String table, String column, String ddl) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(connection.getCatalog(), null, table, column);
        try {
            if (columns.next()) {
                return;
            }
        } finally {
            columns.close();
        }
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate(ddl);
        } finally {
            statement.close();
        }
    }

    private static void insertUser(Connection connection, String nama, String username, String password, String role, String levelAkses, String idKasir) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO pengguna(nama, username, password, role, level_akses, id_kasir) VALUES(?,?,?,?,?,?)");
        try {
            statement.setString(1, nama);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, role);
            statement.setString(5, levelAkses);
            statement.setString(6, idKasir);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    private static void insertMenu(Connection connection, String nama, String kategori, int harga, String fotoPath) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO menu(nama_menu, kategori, harga, tersedia, foto_path, stok) VALUES(?,?,?,true,?,30)");
        try {
            statement.setString(1, nama);
            statement.setString(2, kategori);
            statement.setInt(3, harga);
            statement.setString(4, fotoPath);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    private static void insertMeja(Connection connection, int nomorMeja) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO meja(nomor_meja, status_meja) VALUES(?, 'KOSONG')");
        try {
            statement.setInt(1, nomorMeja);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    private static void insertStok(Connection connection, String nama, int jumlah, String satuan, int minimum) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO stok_bahan(nama_bahan, jumlah_stok, satuan, batas_minimum) VALUES(?,?,?,?)");
        try {
            statement.setString(1, nama);
            statement.setInt(2, jumlah);
            statement.setString(3, satuan);
            statement.setInt(4, minimum);
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }
}
