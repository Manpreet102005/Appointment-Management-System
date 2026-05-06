package validations;

import entities.Person;
import java.util.ArrayList;

public class PersonValidation {

    public static void isAlphabetsOnly(String fullName){
        ArrayList<Character> legalNamingLiterals=new ArrayList<>();
        for(char c='a';c<='z';c++){
            legalNamingLiterals.add(c);
        }
        legalNamingLiterals.add(' ');
        legalNamingLiterals.add('.');
        for(int i=0;i<fullName.length();i++){
            if(!legalNamingLiterals.contains(fullName.toLowerCase().charAt(i))){
                throw new IllegalArgumentException("Name must only contains a~z, A~Z or .(dot)");
            }
        }

    }
    public static void validate(String name){

        if(name==null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name can not be empty.");
        }
        isAlphabetsOnly(name);

    }
}
