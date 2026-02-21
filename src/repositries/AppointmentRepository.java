package repositries;

import entities.Appointment;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public interface AppointmentRepository {
    public void addAppointment(Appointment appointment);
    public boolean cancelAppointment(Appointment appointment);
    public ConcurrentHashMap<Integer, TreeMap<LocalDateTime,Appointment>> getAllAppointments();
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);
}