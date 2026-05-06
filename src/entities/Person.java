package entities;

public class Person {
    private int id;
    private final String fullName;
    public Person(int id,String fullName){
        this.id=id;
        this.fullName=fullName;
    }
    public Person(String fullName){
        this.fullName=fullName;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getFullName(){
        return this.fullName;
    }
    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s",
                id,
                fullName
        );
    }
}
