package repositries;

import entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {
    public void addAppointment(int doctorId, LocalDateTime dateTime, int patientId, String patient);
    public boolean cancelAppointment(Appointment appointment);
    public List<Appointment> getAllAppointments();
}
