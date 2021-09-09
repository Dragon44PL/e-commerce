package org.ecommerce.shop.finance.policies;

import org.ecommerce.shop.finance.exception.UnsupportedLocaleException;
import java.util.Locale;

public interface TaxPolicyFactory {
    TaxPolicy createByLocale(Locale locale) throws UnsupportedLocaleException;
}
