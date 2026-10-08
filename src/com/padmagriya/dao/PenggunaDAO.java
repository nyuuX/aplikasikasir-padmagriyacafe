package com.padmagriya.dao;

import com.padmagriya.model.Admin;
import com.padmagriya.model.Kasir;
import com.padmagriya.model.Owner;
import com.padmagriya.model.Pengguna;
import com.padmagriya.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PenggunaDAO {
    public Pengguna login(String username, String password) throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pengguna WHERE username=? AND password=?");
        try {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            try {
                if (!resultSet.next()) {
                    return null;
                }
                String role = resultSet.getString("role");
                if ("ADMIN".equalsIgnoreCase(role)) {
                    return new Admin(resultSet.getInt("id_pengguna"), resultSet.getString("nama"),
                            resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("level_akses"));
                }
                if ("OWNER".equalsIgnoreCase(role)) {
                    return new Owner(resultSet.getInt("id_pengguna"), resultSet.getString("nama"),
                            resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("level_akses"));
                }
                return new Kasir(resultSet.getInt("id_pengguna"), resultSet.getString("nama"),
                        resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("id_kasir"));
            } finally {
                resultSet.close();
            }
        } finally {
            statement.close();
            connection.close();
        }
    }
}
