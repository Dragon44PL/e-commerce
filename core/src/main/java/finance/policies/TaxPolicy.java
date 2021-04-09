package finance.policies;

import finance.exception.NegativeTaxRatioException;
import finance.vo.Money;
import finance.vo.Tax;

public interface TaxPolicy {
    Tax calculateTax(Money money) throws NegativeTaxRatioException;
}
