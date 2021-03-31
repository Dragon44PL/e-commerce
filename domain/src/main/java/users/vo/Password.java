package users.vo;

import java.time.Instant;

public class Password {

    private final char[] password;
    private final Instant createdAt;
    private final Instant expireAt;

    public Password(String password, Instant expireAt) {
        this(password, Instant.now(), expireAt);
    }

    public Password(String password) {
        this(password, Instant.ofEpochMilli(Long.MAX_VALUE));
    }

    public Password(Password password) {
        this(password.asString(), Instant.from(password.createdAt()), Instant.from(password.expireAt()));
    }

    private Password(String password, Instant createdAt, Instant expireAt) {
        this.password = password.toCharArray();
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }

    public boolean isExpired() {
        final Instant now = Instant.now();
        return now.isAfter(expireAt) || this.createdAt.isAfter(expireAt);
    }

    public Password changePassword(String password) {
        return new Password(password, expireAt);
    }

    public Password changePassword(String password, Instant expireAt) {
        return new Password(password, expireAt);
    }

    public boolean isCorrect(String check) {
        final String current = new String(password);
        return current.equals(check);
    }

    public boolean isBeforeExpired(Password another) {
        return another.expireAt.isBefore(expireAt);
    }

    public String asString() {
        return new String(password);
    }

    public Instant createdAt() {
        return Instant.from(createdAt);
    }

    public Instant expireAt() {
        return Instant.from(expireAt);
    }
}
