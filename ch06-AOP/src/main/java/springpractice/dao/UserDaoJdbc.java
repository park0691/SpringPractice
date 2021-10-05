package springpractice.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springpractice.domain.Level;
import springpractice.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("user_id"));
                    user.setName(rs.getString("user_name"));
                    user.setPw(rs.getString("user_pw"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    return user;
                }
            };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        this.jdbcTemplate.update("INSERT INTO users(user_id, user_pw, user_name, level, login, recommend)" +
                " VALUES(?, ?, ?, ?, ?, ?)", user.getId(), user.getPw(), user.getName(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String userId) throws ClassNotFoundException, SQLException {
        String sql = "select user_id, user_pw, user_name, level, login, recommend from users where user_id = ?";
        return this.jdbcTemplate.queryForObject(sql,
                new Object[]{userId}, this.userMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by user_id", this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                        return conn.prepareStatement("delete from users");
                    }
                }
        );
    }

    public int getCount() {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });
    }

    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set user_name = ?, user_pw = ?, level = ?, login = ?, recommend = ? " +
                        "where user_id = ?", user.getName(), user.getPw(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId()
        );
    }
}