package springpractice.dao;

import springpractice.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void add(User user);
    User get(String id) throws ClassNotFoundException, SQLException;
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}