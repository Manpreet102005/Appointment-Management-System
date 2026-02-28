package entities;

public class Person {
    private final int id;
    private final String fullName;
    public Person(int id,String fullName){
        this.id=id;
        this.fullName=fullName;
    }
    public int getId(){
        return this.id;
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
