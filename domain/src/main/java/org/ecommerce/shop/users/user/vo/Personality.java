package org.ecommerce.shop.users.user.vo;

public record Personality(String firstname, String surname) {

    public Personality changeFirstname(String firstname) {
        return new Personality(firstname, surname);
    }

    public Personality changeSurname(String surname) {
        return new Personality(firstname, surname);
    }
}
