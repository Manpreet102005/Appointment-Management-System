package repositries.impl;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import entities.Appointment;
import repositries.AppointmentRepository;
import util.DatabaseConnection;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        String query = "SELECT COUNT(*) AS count FROM appointments WHERE doctor_id=? AND date_time = ?";
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,doctorId);
            ps.setTimestamp(2,Timestamp.valueOf(dateTime));
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                if(rs.getInt("count")==0) return true;
            }
        }catch(SQLException e){
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Appointment> getAllAppointmentsOf(int doctorId){
        ArrayList<Appointment> allAppointments= new ArrayList<>();
        String query= """
                SELECT a.doctor_id,a.patient_id,
                p.patient_name,a.date_time,
                a.status FROM patients p 
                INNER JOIN appointments a on p.id=a.patient_id 
                WHERE doctor_id= ? 
                ORDER BY a.date_time
                """;
        try(Connection conn= DatabaseConnection.getConnection();
        PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,doctorId);
            ResultSet rs=ps.executeQuery();

            while(rs.next()) {
                allAppointments.add(new Appointment(rs.getInt("doctor_id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        Appointment.Status.valueOf(rs.getString("status")))
                );
            }
        }catch(SQLException e){
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
        return allAppointments;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return List.of();
    }

    @Override
    public boolean appointmentExists(int doctorId, int patientId, LocalDateTime oldDateTime) {
        return false;
    }

    @Override
    public boolean hasAppointmentOnDay(int doctorId, int patientId, LocalDate date) {
        return false;
    }

    @Override
    public Appointment getPatientAppointment(int doctorId, int patienId) {
        return null;
    }
}
