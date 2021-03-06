package org.ecommerce.shop.accounting.account.vo;

import java.time.Instant;

public class Password {

    private final char[] password;
    private final Instant createdAt;
    private final Instant expireAt;

    public Password(String password, Instant expireAt) {
        this(password.toCharArray(), Instant.now(), expireAt);
    }

    public Password(String password) {
        this(password, Instant.ofEpochMilli(Long.MAX_VALUE));
    }

    private Password(char[] password, Instant createdAt, Instant expireAt) {
        this.password = password;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }

    public static Password ofAnother(Password password) {
        return new Password(password.asString().toCharArray(), Instant.from(password.createdAt()), Instant.from(password.expireAt()));
    }

    public boolean isExpired() {
        final Instant now = Instant.now();
        return now.isAfter(expireAt) || this.createdAt.isAfter(expireAt);
    }

    public boolean same(String check) {
        final String current = new String(password);
        return current.equals(check);
    }

    public boolean expireBefore(Password another) {
        return expireAt.isBefore(another.expireAt);
    }

    public String asString() {
        return new String(password);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant expireAt() {
        return expireAt;
    }
}
