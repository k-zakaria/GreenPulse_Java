package entities;

import java.time.LocalDate;

public class Consumption {
    private long id;
    private double value;
    private LocalDate startDate;
    private LocalDate endDate;
    private ConsumptionType type;

    public Consumption(long id, double value, LocalDate startDate, LocalDate endDate, ConsumptionType type) {
        this.id = id;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ConsumptionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Consumption{" +
                "id=" + id +
                ", value=" + value +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type=" + type +
                '}';
    }
}
