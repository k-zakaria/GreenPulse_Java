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
            // Établir une connexion à la base de données
            connection = DatabaseConnection.getInstance().getConnection();

            // Initialiser les repositories et services
            UserRepository userRepository = new UserRepository(connection);
            ConsumptionRepository consumptionRepository = new ConsumptionRepository(connection);
            UserService userService = new UserService(userRepository);
            ConsumptionService consumptionService = new ConsumptionService(consumptionRepository);

            // Initialiser les opérations utilisateur pour gérer les entrées utilisateur
            UserOperations userOperations = new UserOperations(scanner, userService, consumptionService);
            boolean running = true;

            // Boucle principale pour l'interaction utilisateur
            while (running) {
                // Afficher les options du menu
                System.out.println("\nChoisissez une option :");
                System.out.println("1. Ajouter utilisateur");
                System.out.println("2. Afficher utilisateur");
                System.out.println("3. Mettre à jour utilisateur");
                System.out.println("4. Supprimer utilisateur");
                System.out.println("5. Ajouter consommation");
                System.out.println("6. Afficher la consommation totale d'utilisateur");
                System.out.println("7. Calculer l'impact environnemental de l'utilisateur");
                System.out.println("8. Générer un rapport de consommation");
                System.out.println("9. Quitter");


                // Obtenir le choix de l'utilisateur
                int choice = scanner.nextInt();
                scanner.nextLine();

                // Effectuer l'opération en fonction du choix
                if (choice == 9) {
                    System.out.println("Au revoir !");
                    running = false;
                } else {
                    userOperations.performOperation(choice);
                }
            }
        } finally {
            // Assurer la fermeture correcte de la connexion
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
