package validations;

import entities.Person;
import java.util.ArrayList;

public class PersonValidation {
    Person person;
    public PersonValidation(Person person){
        this.person=person;
    }
    public void isAlphabetsOnly(String fullName){
        ArrayList<Character> legalNamingLiterals=new ArrayList<>();
        for(char c='a';c<='z';c++){
            legalNamingLiterals.add(c);
        }
        for(int i=0;i<fullName.length();i++){
            if(!legalNamingLiterals.contains(fullName.toLowerCase().charAt(i))){
                throw new IllegalArgumentException("Name must only contains a~z or A~Z");
            }
        }
    }
    public boolean validate(Person person){
        if(person==null){
            throw new IllegalArgumentException("Person can not be null");
        }
        if(person.getFullName()==null || person.getFullName().trim().isEmpty()){
            throw new IllegalArgumentException("Name can not be empty.");
        }
        isAlphabetsOnly(person.getFullName());
        if(person.getId()<=0){
            throw new IllegalArgumentException("Id can not be negative.");
        }
        return true;
    }
}
