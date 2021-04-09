package finance.policies;

import finance.vo.Money;
import finance.vo.Tax;

public interface TaxPolicy {
    Tax calculateTax(Money money);
}
