package springpractice.template.test;

import springpractice.template.dao.DaoFactory;
import springpractice.template.dao.UserDao;
import springpractice.template.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("bumjin", "박범진", "springno3");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();
        user.setId("pppppp");
        user.setName("테스트유저");
        user.setPw("paaapapa");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
    }

    @Test
    public void getCount() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test
    public void addAndGet2() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPw(), is(user1.getPw()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPw(), is(user2.getPw()));
    }

    @Test(expected= EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User u1, User u2) {
        assertThat(u1.getId(), is(u2.getId()));
        assertThat(u1.getName(), is(u2.getName()));
        assertThat(u1.getPw(), is(u2.getPw()));
    }
}
