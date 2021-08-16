package finance.vo;

public record Tax(Money money, TaxRatio taxRatio) {

    private static final TaxRatio STANDARD = new TaxRatio(100);

    public Money beforeTaxation() {
        final double ratio = STANDARD.ratio(taxRatio);
        return money.multiply(ratio);
    }
}
