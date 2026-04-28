package repositories;

import entities.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {
     void addAppointment(Appointment appointment);

     Appointment.Status cancelAppointment(int doctorId, int appointmentId);

     boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);

     List<Appointment> getAllAppointments();

     Appointment getAppointmentById(int doctorId, int appointmentId);
}