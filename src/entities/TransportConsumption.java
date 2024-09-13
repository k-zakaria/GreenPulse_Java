package entities;

import java.time.LocalDate;

public class TransportConsumption extends Consumption {
    private double distanceTravelled;
    private String vehicleType;

    public TransportConsumption(long id, double value, LocalDate startDate, LocalDate endDate, double distanceTravelled, String vehicleType) {
        super(id, value, startDate, endDate, ConsumptionType.TRANSPORT);
        this.distanceTravelled = distanceTravelled;
        this.vehicleType = vehicleType;
    }

    public double getDistanceTravelled() {
        return distanceTravelled;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return "TransportConsumption{" +
                "id=" + getId() +
                ", value=" + getValue() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                ", distanceTravelled=" + distanceTravelled +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}
