package springpractice.service;

import springpractice.domain.User;

public class TestUserService extends UserServiceImpl {
    private String id;

    public TestUserService(String id) {
        this.id = id;
    }

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new TestUserServiceException();
        super.upgradeLevel(user);
    }
}
