package finance.policies;

import finance.exception.UnsupportedLocaleException;
import java.util.Locale;

public interface TaxPolicyFactory {
    TaxPolicy createByLocale(Locale locale) throws UnsupportedLocaleException;
}
