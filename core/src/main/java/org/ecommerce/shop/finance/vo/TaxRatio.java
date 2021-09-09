package org.ecommerce.shop.finance.vo;

import java.math.BigDecimal;

public record TaxRatio(double percentage) {

    private BigDecimal of(BigDecimal decimal) {
        final BigDecimal ofDouble = BigDecimal.valueOf(percentage);
        return decimal.multiply(ofDouble);
    }

    public Money of(Money money) {
        final BigDecimal value = money.value();
        final BigDecimal calculated = of(value);
        return new Money(calculated, money.currency());
    }

    public double ratio(TaxRatio another) {
        return percentage / another.percentage;
    }

    public TaxRatio difference(TaxRatio taxRatio) {
        final double difference = percentage - taxRatio.percentage;
        return new TaxRatio(difference);
    }

    public TaxRatio absolute(TaxRatio taxRatio) {
        final TaxRatio difference = difference(taxRatio);
        return (difference.isPositive()) ? difference : difference.reverse();
    }

    private TaxRatio reverse() {
        return new TaxRatio(-percentage);
    }

    public boolean isPositive() {
        return percentage > 0;
    }

    public boolean isNonNegative() {
        return percentage >= 0;
    }
}
