package repositries;

import entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {
    public void addAppointment(Appointment appointment);
    public boolean cancelAppointment(Appointment appointment);
    public List<Appointment> getAllAppointments();
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);
}
