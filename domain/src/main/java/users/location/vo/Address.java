package users.location.vo;

public record Address(String street, String city, String region, Country country) {

    public Address changeAddress(String street, String city, String region, Country country) {
        return new Address(street, city, region, country);
    }
}
