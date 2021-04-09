package finance.vo;

import java.math.BigDecimal;

public record TaxRatio(double percentage) {

    private BigDecimal ofDecimal(BigDecimal decimal) {
        final BigDecimal ofDouble = BigDecimal.valueOf(percentage);
        return decimal.multiply(ofDouble);
    }

    public Money ofMoney(Money money) {
        final BigDecimal value = money.value();
        final BigDecimal calculated = ofDecimal(value);
        return new Money(calculated, money.currency());
    }

    public double asRatio(TaxRatio another) {
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
