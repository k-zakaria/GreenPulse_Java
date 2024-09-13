package entities;

import java.time.LocalDate;

public class FoodConsumption extends Consumption {
    private String foodType;
    private double weight;

    public FoodConsumption(long id, double value, LocalDate startDate, LocalDate endDate, String foodType, double weight) {
        super(id, value, startDate, endDate, ConsumptionType.FOOD);
        this.foodType = foodType;
        this.weight = weight;
    }

    public String getFoodType() {
        return foodType;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "FoodConsumption{" +
                "id=" + getId() +
                ", value=" + getValue() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                ", foodType='" + foodType + '\'' +
                ", weight=" + weight +
                '}';
    }
}
