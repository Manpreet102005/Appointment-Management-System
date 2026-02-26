package repositries.impl;

import entities.User;
import repositries.UserRepository;
import validations.PersonValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    ConcurrentHashMap<Integer,User> users=new ConcurrentHashMap<>();

    public synchronized void addUser(User user){
        PersonValidation.validate(user);
        users.put(user.getId(),user);
    }
    public User getUser(int id){
        return users.get(id);
    }
    public synchronized void deleteUser(int id){
        users.remove(id);
    }
    public List<User> getAllUsers(){
        List<User> allUsers=new ArrayList<>();
        for(int id: users.keySet()){
            allUsers.add(users.get(id));
        }
        return allUsers;
    }
}
