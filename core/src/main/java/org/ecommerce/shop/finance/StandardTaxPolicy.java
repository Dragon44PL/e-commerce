package org.ecommerce.shop.finance;

import org.ecommerce.shop.finance.exception.NegativeTaxRatioException;
import org.ecommerce.shop.finance.policies.TaxPolicy;
import org.ecommerce.shop.finance.vo.Money;
import org.ecommerce.shop.finance.vo.TaxRatio;
import org.ecommerce.shop.finance.vo.Tax;

public class StandardTaxPolicy implements TaxPolicy {

    private final TaxRatio taxRatio;

    private StandardTaxPolicy(TaxRatio taxRatio) {
        this.taxRatio = taxRatio;
    }

    public static StandardTaxPolicy of(TaxRatio taxRatio) throws NegativeTaxRatioException {
        if(!taxRatio.isNonNegative()) {
            throw new NegativeTaxRatioException();
        }

        return new StandardTaxPolicy(taxRatio);
    }

    public Tax calculateTax(Money money) {
        final Money calculated = taxRatio.of(money);
        return new Tax(calculated, taxRatio);
    }
}
