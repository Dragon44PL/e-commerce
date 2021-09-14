package org.ecommerce.shop.users.location.vo;

import java.util.Currency;
import java.util.Locale;

public record Country(Locale locale, Currency currency) {

    public Country(Locale locale) {
        this(locale, Currency.getInstance(locale));
    }
}
