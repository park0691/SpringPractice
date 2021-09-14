package com.example.junittesting.test;

import com.example.junittesting.dao.DaoFactory;
import com.example.junittesting.dao.UserDao;
import com.example.junittesting.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.delete("pppppp");
        assertThat(dao.getCount(), is(4));

        User user = new User();
        user.setId("pppppp");
        user.setName("테스트유저");
        user.setPw("paaapapa");

        dao.add(user);
        assertThat(dao.getCount(), is(5));

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));

//        User user2 = dao.get("ssafy");
//
//        assertThat(user2.getName(), is("pp"));
    }
}
