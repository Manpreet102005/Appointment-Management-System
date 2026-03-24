package repositories.impl;

import entities.Appointment;
import repositories.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public Appointment.Status cancelAppointment(int doctorId, int patientId, LocalDate date){
        TreeMap<Integer, Appointment> schedule = appointments.get(doctorId);
        if (schedule == null) return Appointment.Status.BOOKED;

        synchronized (schedule) {
            Appointment a = schedule.get(patientId);
            if (a == null || a.getStatus() == Appointment.Status.CANCELLED) {
                return Appointment.Status.BOOKED;
            }
            if (!a.getDateTime().toLocalDate().equals(date)) {
                return Appointment.Status.BOOKED;
            }
            a.setStatus(Appointment.Status.CANCELLED);
            return Appointment.Status.CANCELLED;
        }
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
    public List<Appointment> getAllAppointmentsOf(int doctorId){
        List<Appointment> result = new ArrayList<>();
        TreeMap<Integer, Appointment> doctorMap = appointments.get(doctorId);
        if(doctorMap == null) return result;
        result.addAll(doctorMap.values());
        return result;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> result = new ArrayList<>();
        for(TreeMap<Integer, Appointment> doctorMap : appointments.values()) {
            result.addAll(doctorMap.values());
        }
        return result;
    }
    @Override
    public boolean appointmentExists(int doctorId, int patientId, LocalDateTime dateTime) {
        TreeMap<Integer,Appointment> doctorMap = appointments.get(doctorId);
        if(doctorMap == null) return false;
        Appointment a = doctorMap.get(patientId);
        if(a == null) return false;
        return a.getDateTime().equals(dateTime);
    }
    @Override
    public boolean hasAppointmentOnDay(int doctorId, int patientId, LocalDate date) {
        TreeMap<Integer, Appointment> schedule = appointments.get(doctorId);
        if (schedule == null) return false;
        for (Appointment a : schedule.values()) {
            if (a.getPatientId() == patientId
                    && a.getDateTime().toLocalDate().equals(date)) return true;
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
