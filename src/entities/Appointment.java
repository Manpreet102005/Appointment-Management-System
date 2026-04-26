package entities;

import java.time.LocalDateTime;

public class Appointment {
    public enum Status{
        BOOKED, CANCELLED;
    }

    private int appointmentId;
    private final int doctorId;
    private final int patientId;
    private final String patientName;
    private final LocalDateTime dateTime;
    private Status status;

    public Appointment(int doctorId,int patientId,String patientName,LocalDateTime dateTime){
        this.doctorId=doctorId;
        this.patientId=patientId;
        this.patientName=patientName;
        this.dateTime=dateTime;
        this.status=Status.CANCELLED;
    }
    public Appointment(int doctorId,int patientId,String patientName,LocalDateTime dateTime, Status status){
        this.doctorId=doctorId;
        this.patientId=patientId;
        this.patientName=patientName;
        this.dateTime=dateTime;
        this.status=status;
    }

    public int getPatientId(){
        return patientId;
    }
    public String getPatientName(){
        return patientName;
    }
    public int getDoctorId(){
        return doctorId;
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
    public int getAppointmentId() { return appointmentId;}
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId;}
    @Override
    public String toString() {
        return String.format(
                "appointmentId: %d | doctorId: %d | patientId: %d | patientName: %s | dateTime: %s | status: %s",
                appointmentId,
                doctorId,
                patientId,
                patientName,
                dateTime,
                status
        );
    }

}
