import config.DatabaseConnection;
import repositories.UserRepository;
import repositories.ConsumptionRepository;
import services.UserService;
import services.ConsumptionService;
import utils.UserOperations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();

            UserRepository userRepository = new UserRepository(connection);
            ConsumptionRepository consumptionRepository = new ConsumptionRepository(connection);
            ConsumptionService consumptionService = new ConsumptionService(consumptionRepository);
            UserService userService = new UserService(userRepository, consumptionService); // Correction ici

            UserOperations userOperations = new UserOperations(scanner, userService, consumptionService);
            boolean running = true;

            while (running) {
                System.out.println("\nChoisissez une option :");
                System.out.println("1. Ajouter utilisateur");
                System.out.println("2. Afficher utilisateur");
                System.out.println("3. Mettre à jour utilisateur");
                System.out.println("4. Supprimer utilisateur");
                System.out.println("5. Ajouter consommation");
                System.out.println("6. Afficher la consommation totale d'utilisateur");
                System.out.println("7. Calculer l'impact environnemental de l'utilisateur");
                System.out.println("8. Générer un rapport de consommation");
                System.out.println("9. Filtrer les utilisateurs par consommation");
                System.out.println("10. Calculer la consommation moyenne d'utilisateur pour une période");
                System.out.println("11. Détection des utilisateurs inactifs");
                System.out.println("12. Trier les utilisateurs par consommation totale");
                System.out.println("13. Quitter");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 13) {
                    System.out.println("Au revoir !");
                    running = false;
                } else {
                    userOperations.performOperation(choice);
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            scanner.close();
        }
    }
}
