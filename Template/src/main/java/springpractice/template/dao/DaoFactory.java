package springpractice.template.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    Factory : 객체의 생성 방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 것
 */
@Configuration  /* 애플리케이션 컨텍스트 or 빈 팩토리가 사용할 설정 정보 표시 */
public class DaoFactory {
    @Bean       /* 오브젝트 생성을 담당하는 IoC 용 메소드 표시 */
    public UserDao userDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        userDao.setJdbcContext(new JdbcContext(connectionMaker));
        return userDao;
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
