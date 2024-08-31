import java.time.LocalDate;

public class Consumption {
    private long id;
    private double value;
    private LocalDate startDate;
    private LocalDate endDate;

    public Consumption(long id, double value, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
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
}
