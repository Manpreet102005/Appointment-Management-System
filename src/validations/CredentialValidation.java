package validations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CredentialValidation {
    private final Set<Character> allowedCharacters;

    public CredentialValidation(){
        allowedCharacters=new HashSet<>();
        addingAllowedCharacters();
    }

    private  void addingAllowedCharacters() {
        for (char c = 'a'; c <= 'z'; c++) {
            allowedCharacters.add(c);
        }
        for(int i=0;i<=9;i++){
            allowedCharacters.add(Character.forDigit(i,10));
        }
        allowedCharacters.add('@');
    }

    public  boolean validate(String username, String password){
        if(username.length()<5 || username.length()>12){
            throw new RuntimeException("Username length should be between 5 and 12.");
        }
        for(char c:username.toCharArray()){
            if(!allowedCharacters.contains(c)){
                throw new RuntimeException("allowed characters for username: a~z, 0~9 and @");
            }
        }
        if(password.length()<8){
            throw new RuntimeException("Password length should be atleast 8");
        }
        return true;
    }


}
