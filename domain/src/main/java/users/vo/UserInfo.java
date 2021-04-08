package users.vo;

public record UserInfo(Personality personality, PhoneNumber phoneNumber) {

    public UserInfo changePhoneNumber() {
        return new UserInfo(personality, phoneNumber);
    }
}
