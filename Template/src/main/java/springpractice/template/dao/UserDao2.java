package springpractice.template.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springpractice.template.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao2 {
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource) {
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    /* 익명 내부 클래스 */
    public void add(final User user) throws ClassNotFoundException, SQLException {
        this.jdbcContext.workWithStatementStrategy(
            new StatementStrategy() {
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement pstmt = c.prepareStatement("INSERT INTO users(user_id, user_pw, user_name)" +
                            " VALUES(?, ?, ?)");
                    pstmt.setString(1, user.getId());
                    pstmt.setString(2, user.getPw());
                    pstmt.setString(3, user.getName());

                    return pstmt;
                }
            }
        );
    }

    public User get(String userId) throws ClassNotFoundException, SQLException {
        Connection conn = dataSource.getConnection();
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

    public void deleteAll() throws ClassNotFoundException, SQLException {
        StatementStrategy st = new DeleteAllStatement();
        this.jdbcContext.workWithStatementStrategy(st);
    }

    /* 변하지 않는 부분을 분리 */
    public void deleteAll2() throws ClassNotFoundException, SQLException {
        this.jdbcContext.executeSql("delete from users");
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("select count(*) from users");
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {

                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            pstmt = stmt.makePreparedStatement(conn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { } }
        }
    }
}