package EnumLists;

public enum PricePlan  {
    NORMAL(100.0),
    PREMIUM(80.0),
    EXCLUSIVE (70.0);
    private final double price;
    PricePlan(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
