package org.ecommerce.shop.users.user.vo;

import java.util.Arrays;

public record PhoneNumber(String prefix, String number) {

    public PhoneNumber(String prefix, int[] number) {
        this(prefix, Arrays.stream(number).toString());
    }

    public PhoneNumber changePrefix(String prefix) {
        return new PhoneNumber(prefix, number);
    }

    public PhoneNumber changeNumber(String number) {
        return new PhoneNumber(prefix, number);
    }

    @Override
    public String toString() {
        return prefix + number;
    }
}
