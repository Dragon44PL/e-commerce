package users.vo;

public record PhoneNumber(int prefix, String number) {

    private static final String PLUS = "+";

    public PhoneNumber changePrefix(int prefix) {
        return new PhoneNumber(prefix, number);
    }

    public PhoneNumber changeNumber(int prefix, String number) {
        return new PhoneNumber(prefix, number);
    }

    public String toPhoneNumber() {
        return PLUS + prefix + number;
    }
}
