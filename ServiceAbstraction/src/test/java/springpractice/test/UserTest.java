package springpractice.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import springpractice.domain.Level;
import springpractice.domain.User;

import static org.hamcrest.CoreMatchers.is;

public class UserTest {
    User user;

    @Before
    public void setUp() {
        this.user = new User();
    }

    @Test
    public void upgradeLevel(){
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            Assert.assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgrdeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
