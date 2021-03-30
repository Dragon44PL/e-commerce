package users.vo;

public class Credentials {
    private final String username;
    private final Password password;

    public Credentials(String username, Password password) {
        this.username = username;
        this.password = password;
    }

    public Credentials changePassword(Password password) {
        return new Credentials(username, password);
    }

    public Credentials changeUsername(String username) {
        return new Credentials(username, password);
    }
}
