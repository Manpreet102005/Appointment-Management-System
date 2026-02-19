package entities;

public class Doctor extends Person{
    private final String specialization;

    public Doctor(int id,String fullName,String specialization){
        super(id,fullName);
        this.specialization=specialization;
    }

    public String getSpecialization(){
        return specialization;
    }
}
