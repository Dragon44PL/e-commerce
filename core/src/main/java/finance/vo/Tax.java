package finance.vo;

public record Tax(Money money, TaxRatio taxRatio) {

    private static final TaxRatio STANDARD = new TaxRatio(100);

    Money beforeTaxation() {
        final double ratio = STANDARD.asRatio(taxRatio);
        return money.multiply(ratio);
    }
}
