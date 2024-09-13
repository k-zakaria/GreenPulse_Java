package entities;

public enum ConsumptionType {
    TRANSPORT , HOUSING, FOOD;

    public String toString() {
        return name().toLowerCase();
    }
}
