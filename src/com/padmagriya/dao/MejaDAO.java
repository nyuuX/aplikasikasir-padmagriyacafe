package com.padmagriya.dao;

import com.padmagriya.model.Meja;
import com.padmagriya.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MejaDAO {
    public List<Meja> findAll() throws SQLException {
        List<Meja> data = new ArrayList<Meja>();
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM meja ORDER BY nomor_meja");
        try {
            while (resultSet.next()) {
                data.add(new Meja(resultSet.getInt("id_meja"), resultSet.getInt("nomor_meja"), resultSet.getString("status_meja")));
            }
            return data;
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
    }

    public void updateStatus(int nomorMeja, String status) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE meja SET status_meja=? WHERE nomor_meja=?");
        try {
            statement.setString(1, status);
            statement.setInt(2, nomorMeja);
            statement.executeUpdate();
        } finally {
            statement.close();
            connection.close();
        }
    }
}
