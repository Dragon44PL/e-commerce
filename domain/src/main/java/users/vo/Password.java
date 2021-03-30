package users.vo;

import java.time.Instant;

public class Password {

    private final char[] password;
    private final Instant createdAt;
    private final Instant expireAt;

    public Password(String password, Instant expireAt) {
        this.password = password.toCharArray();
        this.createdAt = Instant.now();
        this.expireAt = expireAt;
    }

    public Password(String password) {
        this(password, Instant.ofEpochMilli(Long.MAX_VALUE));
    }

    public boolean isExpired() {
        final Instant now = Instant.now();
        return now.isAfter(expireAt) || this.createdAt.isAfter(expireAt);
    }

    public Password changePassword(String password) {
        return new Password(password, expireAt);
    }
}
