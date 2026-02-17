package entities;

import java.time.LocalDateTime;

public class Appointment {
    public enum Status{
        BOOKED, CANCELLED;
    }
    private final int id;
    private final String patient;
    private final String doctor;
    private final LocalDateTime dateTime;
    private Status status;

    public Appointment(int id,String patient,String doctor,LocalDateTime dateTime,Status status){
        this.id=id;
        this.patient=patient;
        this.doctor=doctor;
        this.dateTime=dateTime;
        this.status=status;
    }

    public int getId(){
        return id;
    }
    public String getPatient(){
        return patient;
    }
    public String getDoctor(){
        return doctor;
    }
    public LocalDateTime getDateTime(){
        return dateTime;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status status){
        this.status=status;
    }
}
