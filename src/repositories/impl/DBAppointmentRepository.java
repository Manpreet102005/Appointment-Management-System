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
            ps.setString(4, Appointment.Status.BOOKED.name());
            int res=ps.executeUpdate();
            if (res == 1) {
                appointment.setStatus(Appointment.Status.BOOKED);
            }
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());

        }
    }

    @Override
    public Appointment.Status cancelAppointment(int doctorId, int appointmentId) {
        //Soft Deletion
        String query = """
        UPDATE appointments SET status = 'CANCELLED' 
        WHERE doctor_id = ? AND appointment_id = ?
        and status='BOOKED'
        """;
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(query)){
            ps.setInt(1,doctorId);
            ps.setInt(2,appointmentId);
            int res=ps.executeUpdate();
            if(res>0) return Appointment.Status.CANCELLED;
            else return Appointment.Status.BOOKED;
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
            return Appointment.Status.BOOKED;
        }
    }

    @Override
    public boolean isSlotAvailable(int doctorId, LocalDateTime dateTime){
        String query = """
        SELECT COUNT(*) AS count FROM appointments 
        WHERE doctor_id=? AND date_time = ? AND status=BOOKED
        """;
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

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> allAppointments = new ArrayList<>();
        String query= """
                SELECT a.appointment_id,a.doctor_id,a.patient_id,
                p.patient_name,a.date_time,
                a.status FROM patients p 
                INNER JOIN appointments a on p.id=a.patient_id 
                ORDER BY a.date_time
                """;
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
            ResultSet rs=ps.executeQuery();

            while(rs.next()) {
                allAppointments.add(new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("doctor_id"),
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

    /*
    @Override
    public boolean hasAppointmentOnDay(int doctorId, int patientId, LocalDate date) {
        String query="""
        SELECT EXISTS (SELECT 1 FROM appointments 
        WHERE doctor_id=? AND patient_id=? AND DATE(date_time)=DATE(?)
        AND status='BOOKED')
        """;
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

 */
    @Override
    public Appointment getAppointmentById(int doctorId, int appointmentId){
        String query= """
                SELECT a.appointment_id,a.doctor_id,a.patient_id,
                p.patient_name,a.date_time,
                a.status FROM patients p 
                INNER JOIN appointments a on p.id=a.patient_id 
                WHERE a.doctor_id=? AND a.appointment_id=?
                """;
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,doctorId);
            ps.setInt(2,appointmentId);
            ResultSet rs=ps.executeQuery();

            if(rs.next()) {
                return new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        Appointment.Status.valueOf(rs.getString("status"))
                );
            }
        }catch(SQLException e){
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return null;
    }
}
