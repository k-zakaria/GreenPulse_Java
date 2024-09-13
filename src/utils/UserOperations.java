package utils;

import entities.*;
import services.UserService;
import services.ConsumptionService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UserOperations {
    private Scanner scanner;
    private UserService userService;
    private ConsumptionService consumptionService;

    public UserOperations(Scanner scanner, UserService userService, ConsumptionService consumptionService) {
        this.scanner = scanner;
        this.userService = userService;
        this.consumptionService = consumptionService;
    }

    public void performOperation(int choice) {
        switch (choice) {
            case 1:
                addUser();
                break;
            case 2:
                displayUser();
                break;
            case 3:
                updateUser();
                break;
            case 4:
                deleteUser();
                break;
            case 5:
                addConsumption();
                break;
            case 6:
                displayTotalConsumption();
                break;
            case 7:
                displayImpact();
                break;
            case 8:
                generateConsumptionReport();
                break;
            case 9:
                displayUsersAboveThreshold();
                break;
            case 10:
                displayAverageConsumption();
                break;
            case 11:
                displayInactiveUsers();
                break;
            case 12:
                displayUsersSortedByConsumption();
                break;
            case 13:
                System.out.println("Au revoir !");
                System.exit(0);
                break;
            default:
                System.out.println("Option invalide.");
                break;
        }
    }

    private void addUser() {
        try {
            System.out.print("Entrez l'id : ");
            String userId = scanner.nextLine();
            System.out.print("Entrez le nom : ");
            String userName = scanner.nextLine();
            System.out.print("Entrez l'âge : ");
            int userAge = scanner.nextInt();
            scanner.nextLine();
            userService.add(new User(userId, userName, userAge));
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayUser() {
        try {
            System.out.print("Entrez l'id d'utilisateur à obtenir : ");
            String getUserById = scanner.nextLine();
            User retrievedUser = userService.getById(getUserById);
            if (retrievedUser != null) {
                System.out.println("ID: " + retrievedUser.getId() + ", Nom: " + retrievedUser.getName() + ", Âge: " + retrievedUser.getAge());
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'obtention de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateUser() {
        try {
            System.out.print("Entrez l'id de l'utilisateur à mettre à jour : ");
            String updateById = scanner.nextLine();
            System.out.print("Entrez le nouveau nom : ");
            String newName = scanner.nextLine();
            System.out.print("Entrez le nouvel âge : ");
            int newAge = scanner.nextInt();
            scanner.nextLine();

            User user = new User(updateById, newName, newAge);
            userService.update(user);
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        try {
            System.out.print("Entrez l'id de l'utilisateur à supprimer : ");
            String deleteUserId = scanner.nextLine();
            userService.delete(deleteUserId);
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addConsumption() {
        System.out.print("Entrez l'id de l'utilisateur pour ajouter une consommation : ");
        String userId = scanner.nextLine();
        User user = userService.getById(userId);

        if (user != null) {
            ConsumptionType type = null;
            boolean validType = false;

            while (!validType) {
                System.out.print("Entrez le type de consommation (TRANSPORT, HOUSING, FOOD) : ");
                String typeInput = scanner.nextLine().toUpperCase();

                try {
                    type = ConsumptionType.valueOf(typeInput);
                    validType = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Type de consommation invalide. Veuillez réessayer.");
                }
            }

            System.out.print("Entrez la valeur de la consommation de carbone (par exemple, en kg de CO2) : ");
            double value = scanner.nextDouble();
            scanner.nextLine(); // Consume newline left-over

            System.out.print("Entrez la date de début (format : AAAA-MM-JJ) : ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Entrez la date de fin (format : AAAA-MM-JJ) : ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            Consumption consumption = null;

            switch (type) {
                case TRANSPORT:
                    System.out.print("Entrez la distance parcourue : ");
                    double distance = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Entrez le type de véhicule (car, train) : ");
                    String vehicleType = scanner.nextLine();

                    consumption = new TransportConsumption(1, value, startDate, endDate, distance, vehicleType);
                    break;

                case HOUSING:
                    System.out.print("Entrez la consommation d'énergie : ");
                    double energyConsumption = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Entrez le type d'énergie (electricity, gas) : ");
                    String energyType = scanner.nextLine();

                    consumption = new HousingConsumption(1, value, startDate, endDate, energyConsumption, energyType);
                    break;

                case FOOD:
                    System.out.print("Entrez le type de nourriture (meat, vegetable) : ");
                    String foodType = scanner.nextLine();

                    System.out.print("Entrez le poids : ");
                    double weight = scanner.nextDouble();
                    scanner.nextLine();

                    consumption = new FoodConsumption(1, value, startDate, endDate, foodType, weight);
                    break;
            }

            if (consumption != null) {
                consumptionService.addConsumption(user, consumption);
            } else {
                System.out.println("Type de consommation invalide.");
            }
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }


    public void displayTotalConsumption() {
        System.out.print("Entrez l'id de l'utilisateur pour afficher la consommation totale : ");
        String userId = scanner.nextLine();
        User user = userService.getById(userId);

        if (user != null) {
            double totalConsumption = consumptionService.getTotalConsumptionForUser(user);
            System.out.println("Consommation totale pour l'utilisateur " + user.getName() + " : " + totalConsumption + " unités");
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }


        private void displayImpact() {
        System.out.print("Entrez l'id de l'utilisateur pour afficher l'impact de consommation : ");
        String userId = scanner.nextLine();
        User user = userService.getById(userId);

        if (user != null) {
            consumptionService.displayImpact(user);
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    private void displayUsersAboveThreshold() {
        System.out.print("Entrez le seuil de consommation (en kg CO2eq) : ");
        double threshold = scanner.nextDouble();
        scanner.nextLine();

        List<User> usersAboveThreshold = userService.getUsersAboveConsumptionThreshold(threshold);

        if (usersAboveThreshold.isEmpty()) {
            System.out.println("Aucun utilisateur ne dépasse le seuil de consommation spécifié.");
        } else {
            System.out.println("Utilisateurs avec une consommation supérieure à " + threshold + " kg CO2eq :");
            for (User user : usersAboveThreshold) {
                System.out.println("ID: " + user.getId() + ", Nom: " + user.getName() + ", Âge: " + user.getAge());
            }
        }
    }

    private void displayAverageConsumption() {
        System.out.print("Entrez l'id de l'utilisateur pour calculer la consommation moyenne : ");
        String userId = scanner.nextLine();
        User user = userService.getById(userId);

        if (user != null) {
            System.out.print("Entrez la date de début (format : AAAA-MM-JJ) : ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Entrez la date de fin (format : AAAA-MM-JJ) : ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            double averageConsumption = consumptionService.getAverageConsumptionForUser(user, startDate, endDate);
            System.out.println("Consommation moyenne de carbone pour l'utilisateur " + user.getName() + " entre " + startDate + " et " + endDate + " : " + averageConsumption + " unités");
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    private void displayInactiveUsers() {
        System.out.print("Entrez la date de début (format : AAAA-MM-JJ) : ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Entrez la date de fin (format : AAAA-MM-JJ) : ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        List<User> inactiveUsers = userService.getInactiveUsers(startDate, endDate);

        if (inactiveUsers.isEmpty()) {
            System.out.println("Aucun utilisateur inactif pendant la période spécifiée.");
        } else {
            System.out.println("Utilisateurs inactifs entre " + startDate + " et " + endDate + " :");
            for (User user : inactiveUsers) {
                System.out.println("ID: " + user.getId() + ", Nom: " + user.getName() + ", Âge: " + user.getAge());
            }
        }
    }

    private void displayUsersSortedByConsumption() {
        List<User> sortedUsers = userService.getUsersSortedByTotalConsumption();

        if (sortedUsers.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            System.out.println("Utilisateurs triés par consommation totale de carbone :");
            for (User user : sortedUsers) {
                double totalConsumption = consumptionService.getTotalConsumptionForUser(user);
                System.out.println("ID: " + user.getId() + ", Nom: " + user.getName() + ", Âge: " + user.getAge() + ", Consommation Totale: " + totalConsumption + " unités");
            }
        }
    }

    private void generateConsumptionReport() {
        try {
            System.out.println("Choisissez le type de rapport :");
            System.out.println("1. Quotidien");
            System.out.println("2. Hebdomadaire");
            System.out.println("3. Mensuel");
            System.out.print("Entrez le numéro de votre choix : ");
            int reportType = scanner.nextInt();
            scanner.nextLine(); // Consomme la nouvelle ligne restante

            switch (reportType) {
                case 1:
                    System.out.print("Entrez la date (format : AAAA-MM-JJ) : ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    List<Consumption> dailyReport = consumptionService.generateDailyReport(date);
                    double dailyTotal = consumptionService.calculateTotalConsumption(dailyReport);
                    System.out.println("Rapport quotidien pour le " + date + ":");
                    System.out.println("Consommation totale : " + dailyTotal + " unités");
                    break;

                case 2:
                    System.out.print("Entrez la date de début (format : AAAA-MM-JJ) : ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine());

                    System.out.print("Entrez la date de fin (format : AAAA-MM-JJ) : ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine());

                    List<Consumption> weeklyReport = consumptionService.generateWeeklyReport(startDate, endDate);
                    double weeklyTotal = consumptionService.calculateTotalConsumption(weeklyReport);
                    System.out.println("Rapport hebdomadaire du " + startDate + " au " + endDate + ":");
                    System.out.println("Consommation totale : " + weeklyTotal + " unités");
                    break;

                case 3:
                    System.out.print("Entrez l'année (AAAA) : ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); // Consomme la nouvelle ligne restante

                    System.out.print("Entrez le mois (1-12) : ");
                    int month = scanner.nextInt();
                    scanner.nextLine(); // Consomme la nouvelle ligne restante

                    List<Consumption> monthlyReport = consumptionService.generateMonthlyReport(year, month);
                    double monthlyTotal = consumptionService.calculateTotalConsumption(monthlyReport);
                    System.out.println("Rapport mensuel pour " + year + "-" + month + ":");
                    System.out.println("Consommation totale : " + monthlyTotal + " unités");
                    break;

                default:
                    System.out.println("Choix invalide. Veuillez entrer un numéro entre 1 et 3.");
                    break;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la génération du rapport : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }


}
