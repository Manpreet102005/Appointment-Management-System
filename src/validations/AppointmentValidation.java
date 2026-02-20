package validations;


import entities.Appointment;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;

public class AppointmentValidation  {
    Appointment appointment;
    public AppointmentValidation(Appointment appointment){
        this.appointment=appointment;
    }
    public  boolean validate(Appointment appointment){
        if(appointment==null){
            throw new IllegalArgumentException("Appointment can not be null.");
        }
        if(appointment.getDateTime()==null){
            throw new IllegalArgumentException("Date and time must be filled for appointment.");
        }
        if(appointment.getDateTime().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Date and Time of appointment can not be in past.");
        }
        return true;
    }

}
