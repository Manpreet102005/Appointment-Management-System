package entities;

public class Doctor {
    private final int id;
    private final String fullName;
    private final String specialization;

    public Doctor(int id,String fullName,String specialization){
        this.id=id;
        this.fullName=fullName;
        this.specialization=specialization;
    }

    public int getId() {
        return id;
    }
    public String getFullName(){
        return fullName;
    }
    public String getSpecialization(){
        return specialization;
    }
}
