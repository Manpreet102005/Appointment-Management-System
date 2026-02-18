package repositries.impl;

import entities.Appointment;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppointmentRepository {
    ConcurrentHashMap<Integer, TreeMap<LocalDateTime, Appointment>> appointments=new ConcurrentHashMap<>();

    public Appointment addAppointment(int doctorId,LocalDateTime dateTime,int patientId,String patient){
        appointments.putIfAbsent(doctorId,new TreeMap<>());
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(doctorId);

        synchronized (schedule){
            if(schedule.containsKey(dateTime)){
                throw new IllegalArgumentException("Slot not available.");
            }
            Appointment appointment=new Appointment(doctorId,patientId,patient,dateTime);
            appointment.setStatus(Appointment.Status.BOOKED);
            schedule.put(dateTime,appointment);
            return appointment;
        }
    }
    public boolean cancelAppointment(Appointment appointment){
        if(!appointments.containsKey(appointment.getDoctorId())){
            return false;
        }
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(appointment.getDoctorId());
        if(schedule.containsKey(appointment.getDateTime())) {
            appointments.get(appointment.getDoctorId()).remove(appointment.getDateTime());
            return true;
        }
        return false;
    }
}
