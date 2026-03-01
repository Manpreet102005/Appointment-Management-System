package repositries;

import entities.Appointment;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public interface AppointmentRepository {
     void addAppointment(Appointment appointment);

     Appointment.Status cancelAppointment(int doctorId, int patientId);

     boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);

     TreeMap<Integer, Appointment> getAllAppointmentsOf(int doctorId);

     ConcurrentHashMap<Integer,TreeMap<Integer, Appointment>> getAllAppointments();

     ConcurrentHashMap.KeySetView<Integer, TreeMap<Integer, Appointment>> getAvailableDoctors();

     boolean appointmentExists(int doctorId, int patientId, LocalDateTime oldDateTime);

     Appointment getPatientAppointment(int doctorId, int patienId);

}