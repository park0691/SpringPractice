package springpractice.domain;

public class User {
    Level level;
    int login;
    int recommend;
    String id;
    String name;
    String pw;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public User() { }

    public User(String id, String name, Level level, String pw) {
        this.id = id;
        this.name = name;
        this.pw = pw;
    }

    public User(String id, String name, String pw, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다");
        else this.level = nextLevel;
    }
}
