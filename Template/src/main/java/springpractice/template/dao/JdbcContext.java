package springpractice.template.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = this.dataSource.getConnection();
            pstmt = stmt.makePreparedStatement(conn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { } }
            if (conn != null) { try { conn.close(); } catch (SQLException e) { } }
        }
    }

    public void executeSql(final String query) throws ClassNotFoundException, SQLException {
        workWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        return c.prepareStatement(query);
                    }
                }
        );
    }
}
