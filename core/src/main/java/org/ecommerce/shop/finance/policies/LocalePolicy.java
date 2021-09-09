package org.ecommerce.shop.finance.policies;

import java.util.Locale;

public interface LocalePolicy {
    boolean isAllowedLocale(Locale locale);
}
