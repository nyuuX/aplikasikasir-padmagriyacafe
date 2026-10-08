package com.padmagriya.dao;

import com.padmagriya.model.StokBahan;
import com.padmagriya.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StokBahanDAO {
    public List<StokBahan> findAll() throws SQLException {
        List<StokBahan> data = new ArrayList<StokBahan>();
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM stok_bahan ORDER BY nama_bahan");
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

    public List<StokBahan> findLowStock() throws SQLException {
        List<StokBahan> data = new ArrayList<StokBahan>();
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM stok_bahan WHERE jumlah_stok <= batas_minimum ORDER BY nama_bahan");
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

    public void save(StokBahan stok) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement;
        if (stok.getIdBahan() == 0) {
            statement = connection.prepareStatement("INSERT INTO stok_bahan(nama_bahan, jumlah_stok, satuan, batas_minimum) VALUES(?,?,?,?)");
        } else {
            statement = connection.prepareStatement("UPDATE stok_bahan SET nama_bahan=?, jumlah_stok=?, satuan=?, batas_minimum=? WHERE id_bahan=?");
        }
        try {
            statement.setString(1, stok.getNamaBahan());
            statement.setBigDecimal(2, stok.getJumlahStok());
            statement.setString(3, stok.getSatuan());
            statement.setBigDecimal(4, stok.getBatasMinimum());
            if (stok.getIdBahan() != 0) {
                statement.setInt(5, stok.getIdBahan());
            }
            statement.executeUpdate();
        } finally {
            statement.close();
            connection.close();
        }
    }

    public void delete(int idBahan) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM stok_bahan WHERE id_bahan=?");
        try {
            statement.setInt(1, idBahan);
            statement.executeUpdate();
        } finally {
            statement.close();
            connection.close();
        }
    }

    private StokBahan map(ResultSet resultSet) throws SQLException {
        return new StokBahan(resultSet.getInt("id_bahan"), resultSet.getString("nama_bahan"),
                resultSet.getBigDecimal("jumlah_stok"), resultSet.getString("satuan"), resultSet.getBigDecimal("batas_minimum"));
    }
}
