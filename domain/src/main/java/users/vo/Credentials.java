package users.vo;

public class Credentials {
    private final String username;
    private final Password password;

    public Credentials(String username, Password password) {
        this.username = username;
        this.password = password;
    }

    public static Credentials ofCredentials(Credentials credentials) {
        return new Credentials(credentials.getUsername(), credentials.getPassword());
    }

    public Credentials changePassword(Password candidate) {
        return new Credentials(username, candidate);
    }

    public Credentials changeUsername(String username) {
        return new Credentials(username, password);
    }

    public String getUsername() {
        return username;
    }

    public Password getPassword() {
        return Password.ofAnother(password);
    }
}
