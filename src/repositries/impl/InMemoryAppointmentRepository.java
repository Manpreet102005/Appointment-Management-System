package repositries.impl;

import com.sun.source.tree.Tree;
import entities.Appointment;
import repositries.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public List<Appointment> getAllAppointments(){
        List<Appointment> allAppointments=new ArrayList<>();
        for(int key: appointments.keySet()){
            TreeMap<LocalDateTime,Appointment> schedules=appointments.get(key);
            allAppointments.addAll(schedules.values());
        }
        return allAppointments;
    }
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(doctorId);
        if(schedule.containsKey(dateTime)){
            return false;
        }
        return true;
    }
}
