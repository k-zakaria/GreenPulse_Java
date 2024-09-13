package services;

import entities.User;
import repositories.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
