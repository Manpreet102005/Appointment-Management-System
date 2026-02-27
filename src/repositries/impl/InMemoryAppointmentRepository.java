package repositries.impl;

import entities.Appointment;
import repositries.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppointmentRepository implements AppointmentRepository {
    ConcurrentHashMap<Integer, TreeMap<Integer, Appointment>> appointments=new ConcurrentHashMap<>();
    @Override
    public void addAppointment(Appointment appointment){
        appointments.putIfAbsent(appointment.getDoctorId(),new TreeMap<>());
        TreeMap<Integer,Appointment> schedule=appointments.get(appointment.getDoctorId());

        synchronized (schedule){
            appointment.setStatus(Appointment.Status.BOOKED);
            schedule.put(appointment.getPatientId(),appointment);
        }
    }
    @Override
    public boolean cancelAppointment(Appointment appointment){
        TreeMap<Integer,Appointment> schedule=appointments.get(appointment.getDoctorId());
        if(schedule.containsKey(appointment.getPatientId())) {
            synchronized (schedule){
                appointments.get(appointment.getDoctorId()).remove(appointment.getPatientId());
                return true;
            }
        }
        return false;
    }
    @Override
    public TreeMap<Integer,Appointment> getAllAppointmentsOf(int doctorId){
        return appointments.get(doctorId);
    }
    @Override
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        TreeMap<Integer,Appointment> schedule=appointments.get(doctorId);
        for(Appointment a: schedule.values()){
            if(a.getDateTime().equals(dateTime)) return false;
        }
        return true;
    }
    @Override
    public ConcurrentHashMap.KeySetView<Integer, TreeMap<Integer, Appointment>> getAvailableDoctors(){
        return appointments.keySet();
    }
    @Override
    public ConcurrentHashMap<Integer,TreeMap<Integer, Appointment>> getAllAppointments(){
        return appointments;
    }
    @Override
    public boolean appointmentExists(int doctorId, LocalDateTime dateTime) {
        TreeMap<Integer,Appointment>schedule = appointments.get(doctorId);
        if(schedule==null) return false;
        for(Appointment a: schedule.values()){
            if(a.getDateTime().equals(dateTime)) return true;
        }
        return false;
    }

}
