package springpractice.dao;

import com.mysql.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springpractice.service.UserService;

import javax.sql.DataSource;

/*
    Factory : 객체의 생성 방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 것
 */
@Configuration  /* 애플리케이션 컨텍스트 or 빈 팩토리가 사용할 설정 정보 표시 */
public class DaoFactory {
    @Bean       /* 오브젝트 생성을 담당하는 IoC 용 메소드 표시 */
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }
    @Bean
    public UserService userService() {
        UserService userService = new UserService();
        userService.setUserDao(userDao());  // user dao 생성 이렇게 햐면 되나?
        return userService;
    }
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/springpractice");
        dataSource.setUsername("root");
        dataSource.setPassword("root");


        return dataSource;
    }
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
