package entities;

import java.time.LocalDate;

public class HousingConsumption extends Consumption {
    private double energyConsumption;
    private String energyType;

    public HousingConsumption(long id, double value, LocalDate startDate, LocalDate endDate, double energyConsumption, String energyType) {
        super(id, value, startDate, endDate, ConsumptionType.HOUSING);
        this.energyConsumption = energyConsumption;
        this.energyType = energyType;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public String getEnergyType() {
        return energyType;
    }

    @Override
    public String toString() {
        return "HousingConsumption{" +
                "id=" + getId() +
                ", value=" + getValue() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                ", energyConsumption=" + energyConsumption +
                ", energyType='" + energyType + '\'' +
                '}';
    }
}
