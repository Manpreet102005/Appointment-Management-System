package repositories;

import entities.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {
     void addAppointment(Appointment appointment);

     Appointment.Status cancelAppointment(int doctorId, int appointmentId);

     boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);

     List<Appointment> getAllAppointmentsOf(int doctorId);

     List<Appointment> getAllAppointments();

     boolean appointmentExists(int doctorId, int patientId, LocalDateTime dateTime);

     boolean hasAppointmentOnDay(int doctorId, int patientId, LocalDate date);

     Appointment getPatientAppointment(int doctorId, int patientId);

     Appointment getAppointmentById(int doctorId, int appointmentId);
}