package validations;

import entities.User;

public class UserValidation {
    User user;
    public UserValidation(User user){
        this. user=user;
    }
    public boolean validate(User user){
        if(user.getFullName()==null) {
            throw new IllegalArgumentException("Name can not be empty.");
        }
        if(user.getId()<=0) {
            throw new IllegalArgumentException("Id can not be negative.");
        }
        return true;
    }
}
