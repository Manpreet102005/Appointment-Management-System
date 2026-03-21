package repositries;

import entities.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public interface AppointmentRepository {
     void addAppointment(Appointment appointment);

     Appointment.Status cancelAppointment(int doctorId, int patientId);

     boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);

     List<Appointment> getAllAppointmentsOf(int doctorId);

     List<Appointment> getAllAppointments();

     boolean appointmentExists(int doctorId, int patientId, LocalDateTime oldDateTime);

     boolean hasAppointmentOnDay(int doctorId, int patientId, LocalDate date);

     Appointment getPatientAppointment(int doctorId, int patienId);

}