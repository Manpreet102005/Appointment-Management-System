package repositories.impl;


import entities.Appointment;
import repositories.AppointmentRepository;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DBAppointmentRepository implements AppointmentRepository {
    @Override
    public void addAppointment(Appointment appointment){
        String query = "INSERT INTO appointments (doctor_id, patient_id, date_time, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, appointment.getDoctorId());
            ps.setInt(2, appointment.getPatientId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getDateTime()));
            ps.setString(4, appointment.getStatus().name());
            ps.executeUpdate();
            appointment.setStatus(Appointment.Status.BOOKED);
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
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
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
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
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
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
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return allAppointments;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> allAppointments = new ArrayList<>();
        String query= """
                SELECT a.doctor_id,a.patient_id,
                p.patient_name,a.date_time,
                a.status FROM patients p 
                INNER JOIN appointments a on p.id=a.patient_id 
                ORDER BY a.date_time
                """;
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
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
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return allAppointments;
    }

    @Override
    public boolean appointmentExists(int doctorId, int patientId, LocalDateTime dateTime) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND patient_id=? AND date_time=? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, patientId);
            ps.setTimestamp(3, Timestamp.valueOf(dateTime));
            ResultSet res=ps.executeQuery();
            if (res.next()){
                if(res.getInt(1)>0) return true;
            }
        }catch(SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean hasAppointmentOnDay(int doctorId, int patientId, LocalDate date) {
        String query="SELECT EXISTS (SELECT 1 FROM appointments WHERE doctor_id=? AND patient_id=? AND DATE(date)=?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, doctorId);
            ps.setInt(2, patientId);
            ps.setDate(3, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getBoolean(1);
            }
        } catch(SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return false;
    }

    @Override
    public Appointment getPatientAppointment(int doctorId, int patientId) {
        String query="SELECT * FROM appointments WHERE doctor_id=? AND patient_id=?";
        try(Connection conn= DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)){
            ps.setInt(1,doctorId);
            ps.setInt(2,patientId);
            ResultSet rs=ps.executeQuery();
            return new Appointment(doctorId,patientId,rs.getString("patient_name"),
                    rs.getTimestamp("date_time").toLocalDateTime(),
                    Appointment.Status.valueOf(rs.getString("status")));
        }catch(SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return null;
    }
}
