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
    private static int appointmentId=0;
    ConcurrentHashMap<Integer, TreeMap<Integer, Appointment>> appointments=new ConcurrentHashMap<>();
    @Override
    public Appointment addAppointment(Appointment appointment){
        appointments.putIfAbsent(appointment.getDoctorId(),new TreeMap<>());
        TreeMap<Integer,Appointment> schedule=appointments.get(appointment.getDoctorId());

        synchronized (schedule){
            appointmentId++;
            appointment.setAppointmentId(appointmentId);
            appointment.setStatus(Appointment.Status.BOOKED);
            schedule.put(appointment.getAppointmentId(),appointment);
        }
        return appointment;
    }
    @Override
    public Appointment.Status cancelAppointment(int doctorId, int appointmentId){
        TreeMap<Integer, Appointment> schedule = appointments.get(doctorId);
        if (schedule == null) {
            throw new NoSuchElementException("No appointments found");
        }

        synchronized (schedule) {
            Appointment a = schedule.get(appointmentId);

            if(a==null){
                throw new NoSuchElementException("Appointment not found");
            }
            schedule.remove(appointmentId);
            return Appointment.Status.CANCELLED;
        }
    }
    @Override
    public Appointment rescheduleAppointment(int doctorId,int appointmentId,LocalDateTime newDateTime){
        if (appointments.get(doctorId).get(appointmentId).getStatus() == Appointment.Status.CANCELLED) {
            throw new IllegalStateException("Cannot reschedule cancelled appointment");
        }
        TreeMap<Integer,Appointment> doctorAppointments =appointments.get(doctorId);
        for (Appointment a :doctorAppointments.values()) {
            if (a.getDateTime().equals(newDateTime)
                    && a.getAppointmentId()!=appointmentId
                    && a.getStatus()==Appointment.Status.BOOKED) {
                throw new IllegalStateException("Slot already booked");
            }
        }
        Appointment appointment = appointments.get(doctorId).get(appointmentId);
        appointment.setDateTime(newDateTime);
        return appointment;
    }
    @Override
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        TreeMap<Integer,Appointment> schedule=appointments.get(doctorId);
        if(schedule == null) return true;
        for(Appointment a: schedule.values()){
            if(a.getDateTime().equals(dateTime) && a.getStatus() == Appointment.Status.BOOKED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> result = new ArrayList<>();
        for(TreeMap<Integer, Appointment> doctorMap : appointments.values()) {
            for(Appointment appointment:doctorMap.values()){
                if(appointment.getStatus().equals(Appointment.Status.BOOKED)){
                    result.add(appointment);
                }
            }
        }
        return result;
    }

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
    public Appointment getAppointmentById(int doctorId, int appointmentId) {
        TreeMap<Integer,Appointment> schedule = appointments.get(doctorId);

        if(schedule==null) {
            return null;
        }
        return schedule.get(appointmentId);
    }
}
