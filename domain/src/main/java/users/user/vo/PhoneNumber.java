package users.user.vo;

public record PhoneNumber(String prefix, String number) {

    public PhoneNumber(String number) {
        this("", number);
    }

    public PhoneNumber changePrefix(String prefix) {
        return new PhoneNumber(prefix, number);
    }

    public PhoneNumber changeNumber(String number) {
        return new PhoneNumber(prefix, number);
    }

    public String toPhoneNumber() {
        return prefix + number;
    }
}
