package org.ecommerce.shop.finance.policies;

import org.ecommerce.shop.finance.exception.NegativeTaxRatioException;
import org.ecommerce.shop.finance.vo.Money;
import org.ecommerce.shop.finance.vo.Tax;

public interface TaxPolicy {
    Tax calculateTax(Money money) throws NegativeTaxRatioException;
}
