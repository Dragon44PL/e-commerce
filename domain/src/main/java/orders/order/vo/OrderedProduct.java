package orders.order.vo;

import finance.vo.Money;
import orders.order.exception.NegativeAmountException;

import java.util.UUID;

public record OrderedProduct(UUID productId, Money price, int amount) {

    public static final int MIN_AMOUNT = 1;

    public static OrderedProduct create(UUID productId, Money price) {
        return new OrderedProduct(productId, price, MIN_AMOUNT);
    }

    public Money calculateTotalPrice() {
        return price.multiply(amount);
    }

    public OrderedProduct increaseAmount(int candidate) {
        final int changed = (candidate < 0) ? this.amount : this.amount + candidate;
        return new OrderedProduct(productId, price, changed);
    }

    public OrderedProduct decreaseAmount(int candidate) {

        if(!couldAmountChange(candidate)) {
            throw new NegativeAmountException();
        }

        final int changed = (candidate < 0) ? this.amount : this.amount - candidate;
        return new OrderedProduct(productId, price, changed);
    }

    public OrderedProduct changeAmount(int candidate) {
        return (candidate > 0) ? increaseAmount(candidate) : decreaseAmount(-candidate);
    }

    public boolean couldAmountChange(int candidate) {
        return (amount - candidate) >= MIN_AMOUNT;
    }

}
