package finance.vo;

import java.math.BigDecimal;

public record Percentage(double percentage) {

    public BigDecimal ofDecimal(BigDecimal decimal) {
        final BigDecimal ofDouble = BigDecimal.valueOf(percentage);
        return decimal.multiply(ofDouble);
    }

    public Money ofMoney(Money money) {
        final BigDecimal value = money.value();
        final BigDecimal calculated = ofDecimal(value);
        return new Money(calculated, money.currency());
    }

    public double asRatio(Percentage another) {
        return percentage / another.percentage;
    }

    public Percentage difference(Percentage max) {
        final double difference = percentage - max.percentage;
        return new Percentage(difference);
    }

    public Percentage absolute(Percentage candidate) {
        final Percentage diff = difference(candidate);
        return isPositive() ? diff : new Percentage(diff.percentage * -1);
    }

    public boolean isPositive() {
        return percentage > 0;
    }
}
