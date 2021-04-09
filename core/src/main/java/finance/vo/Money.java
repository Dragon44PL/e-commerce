package finance.vo;

import finance.exception.InvalidCurrencyException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal value, Currency currency) {

    public static final Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    public Money(BigDecimal decimal) {
        this(decimal, DEFAULT_CURRENCY);
    }

    public Money addMoney(Money money) throws InvalidCurrencyException {

        if(!currency.equals(money.currency)) {
            throw new InvalidCurrencyException();
        }

        final BigDecimal bigDecimal = value.add(money.value);
        return new Money(bigDecimal, currency);
    }

    public Money multiply(double multiplier) {
        final BigDecimal decimalMultiplier = BigDecimal.valueOf(multiplier);
        final BigDecimal bigDecimal = value.multiply(decimalMultiplier);
        return new Money(bigDecimal, currency);
    }

    public Money subtract(Money money) throws InvalidCurrencyException {

        if(!currency.equals(money.currency)) {
            throw new InvalidCurrencyException();
        }

        final BigDecimal bigDecimal = value.subtract(money.value);
        return new Money(bigDecimal, currency);
    }

    public Money divide(double divisor) {
        final BigDecimal decimalDivisor = BigDecimal.valueOf(divisor);
        final BigDecimal bigDecimal = value.divide(decimalDivisor, RoundingMode.HALF_EVEN);
        return new Money(bigDecimal, currency);
    }
}