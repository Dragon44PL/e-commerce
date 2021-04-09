package finance;

import finance.exception.TaxRateNotPositiveException;
import finance.policies.TaxPolicy;
import finance.vo.Money;
import finance.vo.Percentage;
import finance.vo.Tax;

public class StandardTaxPolicy implements TaxPolicy {

    private final Percentage percentage;

    private StandardTaxPolicy(Percentage percentage) {
        this.percentage = percentage;
    }

    public StandardTaxPolicy ofTaxPercentage(Percentage percentage) throws TaxRateNotPositiveException {
        if(!percentage.isPositive()) {
            throw new TaxRateNotPositiveException();
        }

        return new StandardTaxPolicy(percentage);
    }

    public Tax calculateTax(Money money) {
        final Money calculated = percentage.ofMoney(money);
        return new Tax(calculated, percentage);
    }
}
