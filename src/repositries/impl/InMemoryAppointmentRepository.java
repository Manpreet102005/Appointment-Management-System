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
    public TreeMap<LocalDateTime,Appointment> getAllAppointmentsOf(int doctorId){
        return appointments.get(doctorId);
    }
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        TreeMap<LocalDateTime,Appointment> schedule=appointments.get(doctorId);
        if(schedule==null) return true;
        return !schedule.containsKey(dateTime);
    }
    public ConcurrentHashMap.KeySetView<Integer, TreeMap<LocalDateTime, Appointment>> getAvailableDoctors(){
        return appointments.keySet();
    }
}
