package orders.order.vo;

public enum OrderStatus {
    PENDING, SHIPPED, CANCELLED, DECLINED, COMPLETED, PROCESSED, DELIVERED;

    public boolean isCapableToProcess() {
        return this == PENDING || this == PROCESSED;
    }
}
