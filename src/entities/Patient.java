package entities;

public class Patient extends Person {

    public Patient(int id, String fullName){
        super(id,fullName);
    }
    public Patient(String fullName){
        super(fullName);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
