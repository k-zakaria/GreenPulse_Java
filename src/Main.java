import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nChoisissez une option :");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Afficher un utilisateur");
            System.out.println("3. Mettre a jour un utilisateur");
            System.out.println("4. Supprimer un utilisateur");
            System.out.println("5. Quitter");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Entrez l'id : ");
                    String id = scanner.nextLine();
                    System.out.print("Entrez le nom : ");
                    String name = scanner.nextLine();
                    System.out.print("Entrez l'age : ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    User user = new User(id, name, age);
                    User.addUser(user);
                    break;
                case 2:
                    System.out.print("Entrez l'id de l'utilisateur a obtenir : ");
                    id = scanner.nextLine();
                    user = User.getUser(id);
                    if (user != null) {
                        System.out.println("ID: " + user.getId() + ", Nom: " + user.getName() + ", Age: " + user.getAge());
                    } else {
                        System.out.println("Utilisateur non trouvé.");
                    }
                    break;
                case 3:
                    System.out.print("Entrez l'id de l'utilisateur mettre a jour : ");
                    id = scanner.nextLine();
                    System.out.print("Entrez le nouveau nom : ");
                    name = scanner.nextLine();
                    System.out.print("Entrez le nouvel age : ");
                    age = scanner.nextInt();
                    scanner.nextLine();
                    User.updateUser(id, name, age);
                    break;
                case 4:
                    System.out.print("Entrez l'id de l'utilisateur a supprimer : ");
                    id = scanner.nextLine();
                    User.deleteUser(id);
                    break;
                case 5:
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
