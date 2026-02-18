package repositries;

import entities.Appointment;

import java.time.LocalDateTime;

public interface AppointmentRepository {
    public void addAppointment(int doctorId, LocalDateTime dateTime, int patientId, String patient);
    public boolean cancelAppointment(Appointment appointment);
}
