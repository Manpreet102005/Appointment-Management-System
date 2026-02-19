package repositries.impl;

import entities.Appointment;
import repositries.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppointmentRepository implements AppointmentRepository {
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
    public List<Appointment> getAllAppointments(){
        List<Appointment> allAppointments=new ArrayList<>();
        for(int key: appointments.keySet()){
            TreeMap<LocalDateTime,Appointment> schedules=appointments.get(key);
            allAppointments.addAll(schedules.values());
        }
        return allAppointments;
    }
}
