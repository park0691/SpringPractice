package com.example.junittesting.dao;

import com.example.junittesting.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;

public class UserDao2 {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springpractice?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", "root", "root");

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
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springpractice?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", "root", "root");

        String sql = "select user_id, user_pw, user_name from users where user_id = ?";
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

        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

//    public void delete(String userId) throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springpractice?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", "root", "root");
//
//        String sql = "delete from users where user_id = ?";
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        pstmt.setString(1, userId);
//
//        pstmt.executeUpdate();
//        pstmt.close();
//        conn.close();
//    }
//
//    public void deleteAll() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springpractice?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", "root", "root");
//
//        String sql = "delete from users";
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        pstmt.executeUpdate();
//
//        pstmt.close();
//        conn.close();
//    }
//
//    public int getCount() throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springpractice?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", "root", "root");
//
//        PreparedStatement pstmt = conn.prepareStatement("select count(*) from users");
//
//        ResultSet rs = pstmt.executeQuery();
//        rs.next();
//        int count = rs.getInt(1);
//
//        rs.close();
//        pstmt.close();
//        conn.close();
//
//        return count;
//    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/springpractice?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", "root", "root");
        return conn;
    }
}
