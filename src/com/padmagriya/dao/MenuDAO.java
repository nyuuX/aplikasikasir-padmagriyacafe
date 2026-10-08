package com.padmagriya.dao;

import com.padmagriya.model.Menu;
import com.padmagriya.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    public List<Menu> findAll() throws SQLException {
        List<Menu> data = new ArrayList<Menu>();
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM menu ORDER BY kategori, nama_menu");
        try {
            while (resultSet.next()) {
                data.add(map(resultSet));
            }
            return data;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public List<Menu> findAvailable() throws SQLException {
        List<Menu> data = new ArrayList<Menu>();
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM menu WHERE tersedia=true AND stok > 0 ORDER BY kategori, nama_menu");
        ResultSet resultSet = statement.executeQuery();
        try {
            while (resultSet.next()) {
                data.add(map(resultSet));
            }
            return data;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public Menu findById(int id) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM menu WHERE id_menu=?");
        try {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            try {
                return resultSet.next() ? map(resultSet) : null;
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
            connection.close();
        }
    }

    public void save(Menu menu) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement;
        if (menu.getIdMenu() == 0) {
            statement = connection.prepareStatement("INSERT INTO menu(nama_menu, kategori, harga, tersedia, foto_path, stok) VALUES(?,?,?,?,?,?)");
        } else {
            statement = connection.prepareStatement("UPDATE menu SET nama_menu=?, kategori=?, harga=?, tersedia=?, foto_path=?, stok=? WHERE id_menu=?");
        }
        try {
            statement.setString(1, menu.getNamaMenu());
            statement.setString(2, menu.getKategori());
            statement.setBigDecimal(3, menu.getHarga());
            statement.setBoolean(4, menu.isTersedia());
            statement.setString(5, menu.getFotoPath());
            statement.setInt(6, menu.getStok());
            if (menu.getIdMenu() != 0) {
                statement.setInt(7, menu.getIdMenu());
            }
            statement.executeUpdate();
        } finally {
            statement.close();
            connection.close();
        }
    }

    public void delete(int idMenu) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM menu WHERE id_menu=?");
        try {
            statement.setInt(1, idMenu);
            statement.executeUpdate();
        } finally {
            statement.close();
            connection.close();
        }
    }

    private Menu map(ResultSet resultSet) throws SQLException {
        return new Menu(resultSet.getInt("id_menu"), resultSet.getString("nama_menu"), resultSet.getString("kategori"),
                resultSet.getBigDecimal("harga"), resultSet.getBoolean("tersedia"), resultSet.getString("foto_path"),
                resultSet.getInt("stok"));
    }
}
