package validations;

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
    public static void validateName(String name){
        if(name==null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name can not be empty.");
        }
        isAlphabetsOnly(name);
    }
    public static void validatePhoneNo(String phoneNo){
        if(phoneNo==null || phoneNo.trim().isEmpty()){
            throw new IllegalArgumentException("Phone No can not be empty.");
        }
        if(phoneNo.length()!=10){
            throw new IllegalArgumentException("Phone No must be 10 digits long.");
        }
        for(int i=0;i<phoneNo.length();i++){
            if(!Character.isDigit(phoneNo.charAt(i))){
                throw new IllegalArgumentException("Phone No must only contains digits");
            }
        }
    }
     public static void validateBloodGroup(String bloodGroup){
         String[] validBloodGroups={"A+","A-","B+","B-","AB+","AB-","O+","O-"};
         for(String bg:validBloodGroups){
             if(bg.equals(bloodGroup)){
                 return;
             }
         }
         throw new IllegalArgumentException("Invalid Blood Group");
     }
     public static void validateGender(char gender) {
         if (gender == 'M' || gender == 'F' || gender == 'O') {
             return;
         }
         throw new IllegalArgumentException(
                 "Invalid Gender. Use 'M' for Male, 'F' for Female and 'O' for Other.");
     }
}
