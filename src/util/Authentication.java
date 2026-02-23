package util;


import repositries.impl.InMemoryAuthorisedUsersRepository;
import validations.CredentialValidation;


public class Authentication {
    private final InMemoryAuthorisedUsersRepository authorisedRepo;
    private final CredentialValidation credentialValidation;

    public Authentication(){
        this.authorisedRepo=new InMemoryAuthorisedUsersRepository();
        this.credentialValidation=new CredentialValidation();
    }

    public void addAuthorisedUser(String username, String password) {
        boolean flag = credentialValidation.validate(username, password);
        if (flag) {
            if(authorisedRepo.authorisedUsers.containsKey(username)) {
                throw new RuntimeException("username already exists.");
            }
            synchronized (authorisedRepo.authorisedUsers) {
                authorisedRepo.authorisedUsers.put(username, password);
            }
        }
    }

    public boolean login (String username, String password){
        if(!authorisedRepo.authorisedUsers.containsKey(username)){
            throw new RuntimeException("No User Found with username: "+username);
        }
        if(password.equals(authorisedRepo.authorisedUsers.get(username))){
            return true;
        }
        throw new RuntimeException("Incorrect Password! Try Again");
    }

    public void changePassword (String username, String currentPassword, String newPassword){
        if(!authorisedRepo.authorisedUsers.containsKey(username)){
            throw new RuntimeException("No user found with username: "+username);
        }
        if(!currentPassword.equals(authorisedRepo.authorisedUsers.get(username))){
            throw new RuntimeException("Password Mismatch! Try Again");
        }
        if(credentialValidation.validate(username,newPassword)) {
            authorisedRepo.authorisedUsers.put(username, newPassword);
        }
    }

    public void removeAuthorisedUser(String username, String password){
        if(!authorisedRepo.authorisedUsers.containsKey(username)){
            throw new RuntimeException("No user found with username: "+username);
        }
        if(!password.equals(authorisedRepo.authorisedUsers.get(username))){
            throw new RuntimeException("Password Mismatch! Try Again");
        }
        authorisedRepo.authorisedUsers.remove(username);
    }

    public void showAuthorisedUsers(){
        if(authorisedRepo.authorisedUsers.isEmpty()){
            System.out.println("No Authorised User Found");
        }
        System.out.println("List Of Authorised Users");
        System.out.println("-".repeat(24));
        for(String username:authorisedRepo.authorisedUsers.keySet()){
            System.out.println(username);
        }
    }


}
