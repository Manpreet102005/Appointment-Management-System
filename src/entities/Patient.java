package entities;

public class Patient extends Person {
    private char gender;
    private String phoneNo;
    private String bloodGroup;

    public Patient(int id, String fullName) {
        super(id, fullName);
    }

    public Patient(String fullName, String phoneNo, char gender, String bloodGroup) {
        super(fullName);
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
    }

    public Patient(int id, String fullName, String phoneNo, char gender, String bloodGroup) {
        super(id, fullName);
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
    }

    public char getGender() {
        return gender;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public String getBloodGroup() {
        return bloodGroup;
    }
    @Override
    public String toString() {
        return super.toString() + " | Phone No: " + phoneNo +
                " | Gender: " + gender +
                " | BloodGroup: " + bloodGroup;
    }
}
