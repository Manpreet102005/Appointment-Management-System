package repositries;

import entities.Appointment;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public interface AppointmentRepository {
     void addAppointment(Appointment appointment);

     boolean cancelAppointment(Appointment appointment);

     boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);

     TreeMap<LocalDateTime, Appointment> getAllAppointmentsOf(int doctorId);

     ConcurrentHashMap<Integer,TreeMap<LocalDateTime, Appointment>> getAllAppointments();

    ConcurrentHashMap.KeySetView<Integer, TreeMap<LocalDateTime, Appointment>> getAvailableDoctors();
}