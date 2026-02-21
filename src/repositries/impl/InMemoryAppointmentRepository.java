package repositries.impl;

import entities.Appointment;
import repositries.AppointmentRepository;

import java.time.LocalDateTime;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppointmentRepository implements AppointmentRepository {
    ConcurrentHashMap<Integer, TreeMap<LocalDateTime, Appointment>> appointments=new ConcurrentHashMap<>();

    public void addAppointment(Appointment appointment){
        appointments.putIfAbsent(appointment.getDoctorId(),new TreeMap<>());
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(appointment.getDoctorId());

        synchronized (schedule){
            appointment.setStatus(Appointment.Status.BOOKED);
            schedule.put(appointment.getDateTime(),appointment);
        }
    }
    public boolean cancelAppointment(Appointment appointment){
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(appointment.getDoctorId());
        if(schedule.containsKey(appointment.getDateTime())) {
            synchronized (schedule){
                appointments.get(appointment.getDoctorId()).remove(appointment.getDateTime());
                return true;
            }
        }
        return false;
    }
    public ConcurrentHashMap<Integer, TreeMap<LocalDateTime, Appointment>> getAllAppointments(){
        return appointments;
    }
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(doctorId);
        return !schedule.containsKey(dateTime);
    }
}
