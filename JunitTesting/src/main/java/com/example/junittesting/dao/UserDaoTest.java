package com.example.junittesting.dao;

import com.example.junittesting.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        UserDao dao = new DaoFactory().userDao();
//        UserDao dao = context.getBean("userDao", UserDao.class);
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("spring");
        user.setName("스프링");
        user.setPw("asdf1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
    }
}
