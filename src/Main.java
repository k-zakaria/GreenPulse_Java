import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nChoisissez une option :");
            System.out.println("1. Ajouter utilisateur");
            System.out.println("2. Afficher utilisateur");
            System.out.println("3. Mettre a jour utilisateur");
            System.out.println("4. Supprimer utilisateur");
            System.out.println("5. Ajouter consommation");
            System.out.println("6. Afficher la consommation totale d'utilisateur");
            System.out.println("7. Generer un rapport de consommation");
            System.out.println("8. Quitter");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Entrez l'id : ");
                    String userId = scanner.nextLine();
                    System.out.print("Entrez le nom : ");
                    String userName = scanner.nextLine();
                    System.out.print("Entrez l'age : ");
                    int userAge = scanner.nextInt();
                    scanner.nextLine();
                    User user = new User(userId, userName, userAge);
                    User.addUser(user);
                    break;
                case 2:
                    System.out.print("Entrez l'id d'utilisateur a obtenir : ");
                    String getUserById = scanner.nextLine();
                    User retrievedUser = User.getUser(getUserById);
                    if (retrievedUser != null) {
                        System.out.println("ID: " + retrievedUser.getId() + ", Nom: " + retrievedUser.getName() + ", Âge: " + retrievedUser.getAge());
                    } else {
                        System.out.println("Utilisateur non trouve.");
                    }
                    break;
                case 3:
                    System.out.print("Entrez l'id de l'utilisateur a mettre a jour : ");
                    String updateUserById = scanner.nextLine();
                    System.out.print("Entrez le nouveau nom : ");
                    String newUserName = scanner.nextLine();
                    System.out.print("Entrez le nouvel age : ");
                    int newUserAge = scanner.nextInt();
                    scanner.nextLine();
                    User.updateUser(updateUserById, newUserName, newUserAge);
                    break;
                case 4:
                    System.out.print("Entrez l'id de l'utilisateur a supprimer : ");
                    String deleteUserId = scanner.nextLine();
                    User.deleteUser(deleteUserId);
                    break;
                case 5:
                    System.out.print("Entrez l'id de l'utilisateur pour ajouter une consommation : ");
                    String consumptionUserId = scanner.nextLine();
                    User consumptionUser = User.getUser(consumptionUserId);
                    if (consumptionUser != null) {
                        System.out.print("Entrez la valeur de la consommation de carbone (par exemple, en kg de CO2) : ");
                        double value = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Entrez la date de debut (format : AAAA-MM-JJ) : ");
                        LocalDate startDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Entrez la date de fin (format : AAAA-MM-JJ) : ");
                        LocalDate endDate = LocalDate.parse(scanner.nextLine());
                        Consumption consumption = new Consumption(1, value, startDate, endDate);
                        consumptionUser.addConsumption(consumption);
                    } else {
                        System.out.println("Utilisateur non trouve.");
                    }
                    break;
                case 6:
                    System.out.print("Entrez l'id de l'utilisateur pour afficher la consommation totale : ");
                    String totalConsumptionUserId = scanner.nextLine();
                    User totalConsumptionUser = User.getUser(totalConsumptionUserId);
                    if (totalConsumptionUser != null) {
                        totalConsumptionUser.displayTotalConsumption();
                    } else {
                        System.out.println("Utilisateur non trouve.");
                    }
                    break;
                case 7:
                    System.out.print("Entrez l'id d'utilisateur pour generer un rapport : ");
                    String reportUserId = scanner.nextLine();
                    User reportUser = User.getUser(reportUserId);
                    if (reportUser != null) {
                        System.out.println("1. Rapport quotidien");
                        System.out.println("2. Rapport hebdomadaire");
                        System.out.println("3. Rapport mensuel");
                        int reportChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (reportChoice) {
                            case 1:
                                System.out.print("Entrez la date (format : AAAA-MM-JJ) : ");
                                LocalDate reportDate = LocalDate.parse(scanner.nextLine());
                                double dailyConsumption = reportUser.getDailyConsumption(reportDate);
                                System.out.println("Consommation quotidienne : " + dailyConsumption + " kg de CO2");
                                break;
                            case 2:
                                System.out.print("Entrez la date de debut de la semaine (format : AAAA-MM-JJ) : ");
                                LocalDate startOfWeek = LocalDate.parse(scanner.nextLine());
                                double weeklyConsumption = reportUser.getWeeklyConsumption(startOfWeek);
                                System.out.println("Consommation hebdomadaire : " + weeklyConsumption + " kg de CO2");
                                break;
                            case 3:
                                System.out.print("Entrez le mois (format : AAAA-MM) : ");
                                LocalDate reportMonth = LocalDate.parse(scanner.nextLine() + "-01");
                                double monthlyConsumption = reportUser.getMonthlyConsumption(reportMonth);
                                System.out.println("Consommation mensuelle : " + monthlyConsumption + " kg de CO2");
                                break;
                            default:
                                System.out.println("Option invalide.");
                        }
                    } else {
                        System.out.println("Utilisateur non trouve.");
                    }
                    break;
                case 8:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Option invalide.");
                    break;
            }
        }

        scanner.close();
    }
}
