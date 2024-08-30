import java.awt.*;
import java.sql.SQLOutput;
import java.util.HashMap;

public class User {

    protected String id;
    protected String name;
    protected int age;
    private static HashMap<String, User> users = new HashMap<>();


    public User(String id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }


    public static void addUser(User user){
        users.put(user.getId(), user);
        System.out.println("Ajouter utilisateur" + user.getName());
    }

    public static User getUser(String id) {
        return users.get(id);
    }

    public static void updateUser(String id, String name, int age){
        User user = users.get(id);
        if(user != null){
            user.setName(name);
            user.setAge(age);
            System.out.println("Utilisateur a modification " + user.getName());
        }else {
            System.out.println("Utilisateur non trouve");
        }
    }

    public static void deleteUser(String id){
        User removedUser = users.remove(id);
        if(removedUser != null){
            System.out.println("utilisateur supprimer" + removedUser.getName());
        }else{
            System.out.println("Utilisateur non trouve");
        }
    }

}

    

