package entities;

public class User {

    public enum Role{
        PATIENT, ADMIN,DOCTOR
    }
    private final int id;
    private final String fullName;
    private final Role role;
    public User(int id, String fullName, Role role){
        this.id=id;
        this.fullName=fullName;
        this.role=role;
    }
    public String getFullName() {return fullName;}
    public int getId(){return id;}
    public Role getRole(){return role;}

}
