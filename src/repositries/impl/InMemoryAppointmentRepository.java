package repositries.impl;

import entities.Appointment;
import repositries.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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
    public Appointment.Status cancelAppointment(int doctorId, int patientId){
        TreeMap<Integer,Appointment> schedule=appointments.get(doctorId);
        if(schedule.containsKey(patientId)) {
            synchronized (schedule){
                appointments.get(doctorId).remove(patientId);
                return Appointment.Status.CANCELLED;
            }
        }
        return Appointment.Status.BOOKED;
    }
    @Override
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        TreeMap<Integer,Appointment> schedule=appointments.get(doctorId);
        if(schedule == null) return true;
        for(Appointment a: schedule.values()){
            if(a.getDateTime().equals(dateTime)) return false;
        }
        return true;
    }
    @Override
    public TreeMap<Integer,Appointment> getAllAppointmentsOf(int doctorId){
        if(appointments.get(doctorId)==null){
            throw new NoSuchElementException("No Appointments for Doctor with ID: "+doctorId);
        }
        return appointments.get(doctorId);
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
    public boolean appointmentExists(int doctorId, int patientId, LocalDateTime dateTime) {
        TreeMap<Integer,Appointment> appointments=getAllAppointmentsOf(doctorId);
        if(appointments==null) return false;
        for(Integer a: appointments.keySet()){
            if(a.equals(patientId) && appointments.get(patientId).getDateTime().equals(dateTime)) return true;
        }
        return false;
    }
    @Override
    public Appointment getPatientAppointment(int doctorId, int patientId){
         Appointment appointment= getAllAppointmentsOf(doctorId).get(patientId);
         if(appointment==null){
             throw new NoSuchElementException("No Appointment Exist");
         }
        return appointment;
    }
}
