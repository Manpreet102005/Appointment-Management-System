package repositries.impl;

import entities.Appointment;
import repositries.AppointmentRepository;
import util.DatabaseConnection;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class DBAppointmentRepository implements AppointmentRepository {
    @Override
    public void addAppointment(Appointment appointment){
        String query = "INSERT INTO appointment (doctor_id, patient_id, date_time, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, appointment.getDoctorId());
            ps.setInt(2, appointment.getPatientId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getDateTime()));
            ps.setString(4, appointment.getStatus().name());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
    }

    public Appointment.Status cancelAppointment(int doctorId, int patientId){
        String query= "DELETE FROM appointments WHERE doctor_id= ? AND patient_id= ?";
        try(Connection conn= DatabaseConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement(query)){
            ps.setInt(1,doctorId);
            ps.setInt(2,patientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
        return Appointment.Status.CANCELLED;
    }
    /*
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime);

    public TreeMap<Integer, Appointment> getAllAppointmentsOf(int doctorId);

    public ConcurrentHashMap<Integer,TreeMap<Integer, Appointment>> getAllAppointments();

    public ConcurrentHashMap.KeySetView<Integer, TreeMap<Integer, Appointment>> getAvailableDoctors();

    public boolean appointmentExists(int doctorId, int patientId, LocalDateTime oldDateTime);

    public Appointment getPatientAppointment(int doctorId, int patienId);*/
}
