package entities;

public class User extends Person {

    public enum Role{
        PATIENT, ADMIN
    }
    private final Role role;
    public User(int id, String fullName){
        super(id,fullName);
        this.role=role;
    }

    public Role getRole(){return role;}
}
