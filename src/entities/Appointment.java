package entities;

import java.time.LocalDateTime;

public class Appointment {
    public enum Status{
        BOOKED, CANCELLED;
    }
    private final int appointmentId;
    private final int doctorId;
    private final int patientId;
    private final String patientName;
    private final LocalDateTime dateTime;
    private Status status;

    private static int counter=1;
    public Appointment(int doctorId,int patientId,String patientName,LocalDateTime dateTime, Status status){
        this.appointmentId=counter++;
        this.doctorId=doctorId;
        this.patientId=patientId;
        this.patientName=patientName;
        this.dateTime=dateTime;
        this.status=status;
    }
    public Appointment(int appointmentId,int doctorId,int patientId,String patientName,LocalDateTime dateTime, Status status){
        this.appointmentId=appointmentId;
        this.doctorId=doctorId;
        this.patientId=patientId;
        this.patientName=patientName;
        this.dateTime=dateTime;
        this.status=status;
    }


    public int getAppointmentId(){ return appointmentId;}
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
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", doctorId=" + doctorId +
                ", patientId=" + patientId +
                ", patientName='" + patientName +
                ", dateTime=" + dateTime +
                ", status=" + status +
                '}';
    }

}
