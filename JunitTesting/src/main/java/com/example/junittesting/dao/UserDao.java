package com.example.junittesting.dao;

import com.example.junittesting.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection conn = connectionMaker.makeConnection();
        String sql = "INSERT INTO users(user_id, user_pw, user_name)" +
                " VALUES(?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getPw());
        pstmt.setString(3, user.getName());

        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    public User get(String userId) throws ClassNotFoundException, SQLException {
        Connection conn = connectionMaker.makeConnection();
        String sql = "select user_id, user_pw, user_name, user_address, user_email from users where user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);

        ResultSet rs = pstmt.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("user_id"));
            user.setPw(rs.getString("user_pw"));
            user.setName(rs.getString("user_name"));
        }

        rs.close();
        pstmt.close();
        conn.close();

        return user;
    }

    public void delete(String userId) throws ClassNotFoundException, SQLException {
        Connection conn = connectionMaker.makeConnection();
        String sql = "delete from users where user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);

        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    public void deleteAll() throws ClassNotFoundException, SQLException {
        Connection conn = connectionMaker.makeConnection();
        String sql = "delete from users";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        Connection conn = connectionMaker.makeConnection();
        PreparedStatement pstmt = conn.prepareStatement("select count(*) from users");

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        pstmt.close();
        conn.close();

        return count;
    }
}
