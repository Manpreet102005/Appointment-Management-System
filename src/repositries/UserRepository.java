package repositries;

import entities.Doctor;
import entities.User;

import java.util.List;

public interface UserRepository {
    public void addUser(User user);
    public List<User> getAllUsers();
    public User getUser(int id);
    public void deleteUser(int id);
}
