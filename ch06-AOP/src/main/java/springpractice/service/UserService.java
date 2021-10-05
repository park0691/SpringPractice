package springpractice.service;

import springpractice.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels() throws Exception;
}
