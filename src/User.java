import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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
        users.put(user.getId(), user);
        System.out.println("Ajouter utilisateur " + user.getName());
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

        public void addConsumption(Consumption consumption) {
            this.consumption.add(consumption);
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
        double total = 0.0;
        for (Consumption cons : consumption) {
            if (!cons.getStartDate().isAfter(date) && !cons.getEndDate().isBefore(date)) {
                total += cons.getValue();
            }
        }
        return total;
    }

    public double getWeeklyConsumption(LocalDate startOfWeek) {
        double total = 0.0;
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        for (Consumption cons : consumption) {
            if (!cons.getStartDate().isAfter(endOfWeek) && !cons.getEndDate().isBefore(startOfWeek)) {
                total += cons.getValue();
            }
        }
        return total;
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
