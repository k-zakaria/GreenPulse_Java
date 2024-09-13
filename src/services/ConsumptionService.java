package services;

import entities.*;
import repositories.ConsumptionRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConsumptionService {
    private ConsumptionRepository consumptionRepository;

    public ConsumptionService(ConsumptionRepository consumptionRepository) {
        this.consumptionRepository = consumptionRepository;
    }

    public void addConsumption(User user, Consumption consumption) {
        try {
            consumptionRepository.addConsumption(consumption, user);
            System.out.println("Consommation ajoutée pour " + user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de la consommation.");
        }
    }

    public double getTotalConsumptionForUser(User user) {
        List<Consumption> consumptions = consumptionRepository.getConsumptionsByUser(user);
        return consumptions.stream()
                .mapToDouble(Consumption::getValue)
                .sum();
    }


    public void displayTotalConsumption(User user) {
        double totalConsumption = getTotalConsumptionForUser(user);
        System.out.println("Consommation totale de carbone pour " + user.getName() + " : " + totalConsumption + " kg de CO2");
    }

    public double calculateImpactForUser(User user) {
        List<Consumption> consumptions = consumptionRepository.getConsumptionsByUser(user);
        double totalImpact = 0.0;

        for (Consumption consumption : consumptions) {
            switch (consumption.getType()) {
                case TRANSPORT:
                    TransportConsumption transport = (TransportConsumption) consumption;
                    totalImpact += transport.getDistanceTravelled() * ("car".equalsIgnoreCase(transport.getVehicleType()) ? 0.5 : 0.1);
                    break;
                case HOUSING:
                    HousingConsumption housing = (HousingConsumption) consumption;
                    totalImpact += housing.getEnergyConsumption() * ("electricity".equalsIgnoreCase(housing.getEnergyType()) ? 1.5 : 2.0);
                    break;
                case FOOD:
                    FoodConsumption food = (FoodConsumption) consumption;
                    totalImpact += food.getWeight() * ("meat".equalsIgnoreCase(food.getFoodType()) ? 5.0 : 0.5);
                    break;
                default:
                    System.out.println("Type de consommation non pris en charge pour l'ID: " + consumption.getId());
                    break;
            }
        }

        return totalImpact;
    }

    public void displayImpact(User user) {
        double totalImpact = calculateImpactForUser(user);
        System.out.println("Impact total de consommation de carbone pour " + user.getName() + " : " + totalImpact + " kg de CO2");
    }

    public double getAverageConsumptionForUser(User user, LocalDate startDate, LocalDate endDate) {
        List<Consumption> consumptions = consumptionRepository.getConsumptionsByUser(user);

        double averageConsumption = consumptions.stream()
                .filter(consumption ->
                        !consumption.getStartDate().isAfter(endDate) &&
                                !consumption.getEndDate().isBefore(startDate))
                .mapToDouble(Consumption::getValue)
                .average()
                .orElse(0.0);

        return averageConsumption;
    }

    public List<Consumption> getConsumptionsForUser(User user, LocalDate startDate, LocalDate endDate) {
        List<Consumption> consumptions = consumptionRepository.getConsumptionsByUser(user);

        return consumptions.stream()
                .filter(consumption -> !consumption.getStartDate().isAfter(endDate) && !consumption.getEndDate().isBefore(startDate))
                .collect(Collectors.toList());
    }

    public List<Consumption> generateDailyReport(LocalDate date) throws SQLException {
        return consumptionRepository.getDailyConsumption(date);
    }

    public List<Consumption> generateWeeklyReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        return consumptionRepository.getWeeklyConsumption(startDate, endDate);
    }

    public List<Consumption> generateMonthlyReport(int year, int month) throws SQLException {
        return consumptionRepository.getMonthlyConsumption(year, month);
    }

    public double calculateTotalConsumption(List<Consumption> consumptions) {
        return consumptions.stream()
                .mapToDouble(Consumption::getValue)
                .sum();
    }
}
