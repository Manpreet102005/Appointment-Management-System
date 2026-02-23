package util;


import validations.CredentialValidation;

import java.util.concurrent.ConcurrentHashMap;

public class Authentication {
    private final ConcurrentHashMap<String,String> authorisedUsers;

    public Authentication(ConcurrentHashMap<String,String> authorisedUsers){
        this.authorisedUsers=authorisedUsers;
    }
    public void addAuthorisedUser(String username, String password) {
        boolean flag = CredentialValidation.validate(username, password);
        if (flag) {
            if(authorisedUsers.containsKey(username)) {
                throw new RuntimeException("username already exists.");
            }
            synchronized (authorisedUsers) {
                authorisedUsers.put(username, password);
            }
        }
    }

    public boolean login (String username, String password){
        if(!authorisedUsers.containsKey(username)){
            throw new RuntimeException("No User Found with username: "+username);
        }
        if(password.equals(authorisedUsers.get(username))){
            return true;
        }
        throw new RuntimeException("Incorrect Password! Try Again");
    }

    public void changePassword (String username, String currentPassword, String newPassword){
        if(!authorisedUsers.containsKey(username)){
            throw new RuntimeException("No user found with username: "+username);
        }
        if(!currentPassword.equals(authorisedUsers.get(username))){
            throw new RuntimeException("Password Mismatch! Try Again");
        }
        if(CredentialValidation.validate(username,newPassword)) {
            authorisedUsers.put(username, newPassword);
        }
    }

    public void removeAuthorisedUser(String username, String password){
        if(!authorisedUsers.containsKey(username)){
            throw new RuntimeException("No user found with username: "+username);
        }
        if(!password.equals(authorisedUsers.get(username))){
            throw new RuntimeException("Password Mismatch! Try Again");
        }
        authorisedUsers.remove(username);
    }

    public void showAuthorisedUsers(){
        if(authorisedUsers.isEmpty()){
            System.out.println("No Authorised User Found");
        }
        System.out.println("List Of Authorised Users");
        System.out.println("-".repeat(24));
        for(String username:authorisedUsers.keySet()){
            System.out.println(username);
        }
    }
}
