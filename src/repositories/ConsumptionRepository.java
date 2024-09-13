package repositories;

import entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsumptionRepository {
    private Connection connection;

    public ConsumptionRepository(Connection connection) {
        this.connection = connection;
    }

    public void addConsumption(Consumption consumption, User user) throws SQLException {
        String insertConsumptionQuery = "INSERT INTO consumption (value, start_date, end_date, type, user_id) VALUES (?, ?, ?, ?::consumption_type, ?) RETURNING id";
        long consumptionId;
        int userId = Integer.parseInt(user.getId());

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertConsumptionQuery)) {
            preparedStatement.setDouble(1, consumption.getValue());
            preparedStatement.setDate(2, java.sql.Date.valueOf(consumption.getStartDate()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(consumption.getEndDate()));

            preparedStatement.setString(4, consumption.getType().toString()); // Assurez-vous que toString() renvoie le format correct
            preparedStatement.setInt(5, userId);

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    consumptionId = generatedKeys.getLong(1);
                    addTypeSpecificDetails(consumptionId, consumption);
                } else {
                    throw new SQLException("Creating consumption failed, no ID obtained.");
                }
            }
        }
    }

    private void addTypeSpecificDetails(long consumptionId, Consumption consumption) throws SQLException {
        String query = "";
        switch (consumption.getType()) {
            case TRANSPORT:
                query = "INSERT INTO transport (id, distance_travelled, vehicle_type) VALUES (?, ?, ?)";
                break;
            case HOUSING:
                query = "INSERT INTO housing (id, energy_consumption, energy_type) VALUES (?, ?, ?)";
                break;
            case FOOD:
                query = "INSERT INTO food (id, food_type, weight) VALUES (?, ?, ?)";
                break;
        }

        if (query.isEmpty()) return;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, consumptionId);
            switch (consumption.getType()) {
                case TRANSPORT:
                    preparedStatement.setDouble(2, ((TransportConsumption) consumption).getDistanceTravelled());
                    preparedStatement.setString(3, ((TransportConsumption) consumption).getVehicleType());
                    break;
                case HOUSING:
                    preparedStatement.setDouble(2, ((HousingConsumption) consumption).getEnergyConsumption());
                    preparedStatement.setString(3, ((HousingConsumption) consumption).getEnergyType());
                    break;
                case FOOD:
                    preparedStatement.setString(2, ((FoodConsumption) consumption).getFoodType());
                    preparedStatement.setDouble(3, ((FoodConsumption) consumption).getWeight());
                    break;
            }
            preparedStatement.executeUpdate();
        }
    }

    public List<Consumption> getConsumptionsByUser(User user) {
        List<Consumption> consumptions = new ArrayList<>();
        String query = "SELECT id, value, start_date, end_date, type::text AS type FROM consumption WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(user.getId()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                double value = resultSet.getDouble("value");
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
                ConsumptionType type = ConsumptionType.valueOf(resultSet.getString("type").toUpperCase());

                Consumption consumption = null;
                switch (type) {
                    case TRANSPORT:
                        consumption = getTransportConsumption(id, value, startDate, endDate);
                        break;
                    case HOUSING:
                        consumption = getHousingConsumption(id, value, startDate, endDate);
                        break;
                    case FOOD:
                        consumption = getFoodConsumption(id, value, startDate, endDate);
                        break;
                    default:
                        System.err.println("Unknown consumption type: " + type);
                        break;
                }

                if (consumption != null) {
                    consumptions.add(consumption);
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return consumptions;
    }


    private TransportConsumption getTransportConsumption(long id, double value, LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT distance_travelled, vehicle_type FROM transport WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double distanceTravelled = resultSet.getDouble("distance_travelled");
                String vehicleType = resultSet.getString("vehicle_type");
                return new TransportConsumption(id, value, startDate, endDate, distanceTravelled, vehicleType);
            }
        }
        return null;
    }


    private HousingConsumption getHousingConsumption(long id, double value, LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT energy_consumption, energy_type FROM housing WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double energyConsumption = resultSet.getDouble("energy_consumption");
                String energyType = resultSet.getString("energy_type");
                return new HousingConsumption(id, value, startDate, endDate, energyConsumption, energyType);
            }
        }
        return null;
    }


    private FoodConsumption getFoodConsumption(long id, double value, LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT food_type, weight FROM food WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String foodType = resultSet.getString("food_type");
                double weight = resultSet.getDouble("weight");
                return new FoodConsumption(id, value, startDate, endDate, foodType, weight);
            }
        }
        return null;
    }

    public List<Consumption> getDailyConsumption(LocalDate date) throws SQLException {
        String query = "SELECT * FROM consumption WHERE DATE(start_date) = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractConsumptions(resultSet);
        }
    }

    public List<Consumption> getWeeklyConsumption(LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT * FROM consumption WHERE start_date BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractConsumptions(resultSet);
        }
    }

    public List<Consumption> getMonthlyConsumption(int year, int month) throws SQLException {
        String query = "SELECT * FROM consumption WHERE EXTRACT(YEAR FROM start_date) = ? AND EXTRACT(MONTH FROM start_date) = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractConsumptions(resultSet);
        }
    }

    private List<Consumption> extractConsumptions(ResultSet resultSet) throws SQLException {
        List<Consumption> consumptions = new ArrayList<>();
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            double value = resultSet.getDouble("value");
            LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
            LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
            String type = resultSet.getString("type");
            Consumption consumption = new Consumption(id, value, startDate, endDate, ConsumptionType.valueOf(type.toUpperCase()));
            consumptions.add(consumption);
        }
        return consumptions;
    }



}
