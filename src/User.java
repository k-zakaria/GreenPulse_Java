import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class User {
    protected String id;
    protected String name;
    protected int age;
    private static HashMap<String, User> users = new HashMap<>();
    private ArrayList<Consumption> consumption = new ArrayList<>();



    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void addUser(User user) {
        String userId = user.getId();
        if (users.containsKey(userId)) {
            System.out.println("Erreur : L'utilisateur avec l'ID " + userId + " existe déjà.");
        } else {
            users.put(userId, user);
            System.out.println("Utilisateur ajouté : " + user.getName());
        }

    }

    public static User getUser(String id) {
        return users.get(id);
    }

    public static void updateUser(String id, String name, int age) {
        User user = users.get(id);
        if (user != null) {
            user.setName(name);
            user.setAge(age);
            System.out.println("Utilisateur modifie : " + user.getName());
        } else {
            System.out.println("Utilisateur non trouve");
        }
    }

    public static void deleteUser(String id) {
        User removedUser = users.remove(id);
        if (removedUser != null) {
            System.out.println("Utilisateur supprime : " + removedUser.getName());
        } else {
            System.out.println("Utilisateur non trouve");
        }
    }

    public void addConsumption(Consumption newConsumption) {
        LocalDate newStartDate = newConsumption.getStartDate();
        LocalDate newEndDate = newConsumption.getEndDate();

        if (newStartDate.isAfter(newEndDate)) {
            System.out.println("Erreur : la date de debut (" + newStartDate + ") est apres la date de fin (" + newEndDate + ").");
            return;
        }
        for (Consumption cons : consumption) {
            LocalDate startDate = cons.getStartDate();
            LocalDate endDate = cons.getEndDate();

            if (!newEndDate.isBefore(startDate) && !newStartDate.isAfter(endDate)) {
                System.out.println("Erreur : La periode de consommation proposée existe.");
                return;
            }
        }

        this.consumption.add(newConsumption);
        System.out.println("Consommation ajoutée pour " + this.getName());
    }

    public void displayTotalConsumption() {
        double total = 0.0;
        for (Consumption cons : consumption) {
            total += cons.getValue();
        }
        System.out.println("Consommation totale de carbone pour " + name + " : " + total + " kg de CO2");
    }

    public double getDailyConsumption(LocalDate date) {
        double totalConsumption = 0.0;

        for (Consumption cons : consumption) {
            LocalDate startDate = cons.getStartDate();
            LocalDate endDate = cons.getEndDate();
            double value = cons.getValue();

            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                long daysBetween = startDate.until(endDate).getDays() + 1;
                double dailyConsumption = value / daysBetween;
                totalConsumption += dailyConsumption;
            }
        }
        return totalConsumption;
    }

    public double getWeeklyConsumption(LocalDate startOfWeek) {
        double total = 0.0;
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return consumption.stream()
                .filter(cons -> !cons.getStartDate().isAfter(endOfWeek) && !cons.getEndDate().isBefore(startOfWeek))
                .mapToDouble(Consumption::getValue)
                .sum();
    }

    public double getMonthlyConsumption(LocalDate month) {
        double total = 0.0;
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        for (Consumption cons : consumption) {
            if (!cons.getStartDate().isAfter(endOfMonth) && !cons.getEndDate().isBefore(startOfMonth)) {
                total += cons.getValue();
            }
        }
        return total;
    }
}