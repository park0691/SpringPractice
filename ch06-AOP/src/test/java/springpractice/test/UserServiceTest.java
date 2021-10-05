package springpractice.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import springpractice.dao.UserDao;
import springpractice.dao.UserDaoJdbc;
import springpractice.domain.Level;
import springpractice.domain.User;
import springpractice.proxy.TransactionHandler;
import springpractice.service.*;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springpractice.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springpractice.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
    @Autowired
    ProxyFactoryBean userService;
    @Autowired
    UserDaoJdbc userDao;
    @Autowired
    DataSource dataSource;
    @Autowired
    PlatformTransactionManager platformTransactionManager;
    @Autowired
    ApplicationContext context;
    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p1", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p1", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p1", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }
    
    @Test
    public void upgradeLevels() throws Exception {
        UserServiceImpl userService = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userService.setUserDao(mockUserDao);

        userService.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

//    @Test
//    public void add() throws SQLException, ClassNotFoundException {
//        userDao.deleteAll();
//
//        User userWithLevel = users.get(4);
//        User userWithoutLevel = users.get(0);
//        userWithoutLevel.setLevel(null);
//
//        userService.add(userWithLevel);
//        userService.add(userWithoutLevel);
//
//        User userWithLevelRead = userDao.get(userWithLevel.getId());
//        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
//
//        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
//        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
//    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);

        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();


        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch(TestUserServiceException e) {

        }

        checkLevelUpgraded(users.get(1), false);
    }
    private void checkLevelUpgraded(User user, boolean upgraded) throws SQLException, ClassNotFoundException {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        public void update(User user) {
            updated.add(user);
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) throws ClassNotFoundException, SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
}