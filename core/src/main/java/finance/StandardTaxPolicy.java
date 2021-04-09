package finance;

import finance.exception.NegativeTaxRatioException;
import finance.policies.TaxPolicy;
import finance.vo.Money;
import finance.vo.TaxRatio;
import finance.vo.Tax;

public class StandardTaxPolicy implements TaxPolicy {

    private final TaxRatio taxRatio;

    private StandardTaxPolicy(TaxRatio taxRatio) {
        this.taxRatio = taxRatio;
    }

    public StandardTaxPolicy ofTaxPercentage(TaxRatio taxRatio) throws NegativeTaxRatioException {
        if(!taxRatio.isNonNegative()) {
            throw new NegativeTaxRatioException();
        }

        return new StandardTaxPolicy(taxRatio);
    }

    public Tax calculateTax(Money money) {
        final Money calculated = taxRatio.ofMoney(money);
        return new Tax(calculated, taxRatio);
    }
}
