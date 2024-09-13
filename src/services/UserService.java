package services;

import entities.Consumption;
import entities.User;
import repositories.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private UserRepository userRepository;
    private ConsumptionService consumptionService;


    public UserService(UserRepository userRepository, ConsumptionService consumptionService) {
        this.userRepository = userRepository;
        this.consumptionService = consumptionService;
    }

    public void add(User user) {
        userRepository.addUser(user);
    }

    public User getById(String id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    public void update(User user) {
        userRepository.updateUser(user);
    }

    public void delete(String id) {
        userRepository.deleteUser(id);
    }

    public List<User> getUsersAboveConsumptionThreshold(double threshold) {
        List<User> allUsers = userRepository.getAllUsers();
        return allUsers.stream()
                .filter(user -> {
                    double totalConsumption = consumptionService.getTotalConsumptionForUser(user);
                    return totalConsumption > threshold;
                })
                .collect(Collectors.toList());
    }

    public List<User> getInactiveUsers(LocalDate startDate, LocalDate endDate) {
        List<User> allUsers = userRepository.getAllUsers();

        return allUsers.stream()
                .filter(user -> {
                    List<Consumption> consumptions = consumptionService.getConsumptionsForUser(user, startDate, endDate);
                    return consumptions.isEmpty();
                })
                .collect(Collectors.toList());
    }

    public List<User> getUsersSortedByTotalConsumption() {
        List<User> allUsers = userRepository.getAllUsers();

        return allUsers.stream()
                .sorted(Comparator.comparingDouble(user ->
                        consumptionService.getTotalConsumptionForUser(user)))
                .collect(Collectors.toList());
    }
}
