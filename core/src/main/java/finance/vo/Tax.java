package finance.vo;

public record Tax(Money money, Percentage percentage) {

    private static final Percentage STANDARD = new Percentage(100);

    Money beforeTaxation() {
        final double ratio = STANDARD.asRatio(percentage);
        return money.multiply(ratio);
    }
}
