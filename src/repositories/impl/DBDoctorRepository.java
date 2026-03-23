package repositories.impl;

import entities.Doctor;
import repositories.DoctorRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBDoctorRepository implements DoctorRepository {
    public boolean addDoctor(Doctor doctor){
        String query="INSERT INTO doctors (doctor_id, doctor_name,specialisation) VALUES (?,?,?)";
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,doctor.getId());
            ps.setString(2,doctor.getFullName());
            ps.setString(3,doctor.getSpecialization());
            int rows=ps.executeUpdate();
            return rows==1;
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
            return false;
        }
    }
    public boolean removeDoctor(int id){
        String query= "DELETE FROM doctors WHERE doctor_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1,id);
            int rows=ps.executeUpdate();
            return rows==1;
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
            return false;
        }
    }
    public Doctor getDoctor(int id){
        String query = "SELECT * FROM doctors WHERE doctor_id=?";
        Doctor doctor = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("doctor_id"),
                        rs.getString("doctor_name"),
                        rs.getString("specialisation")
                );
            }
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return doctor;
    }
    public List<Doctor> getAllDoctors(){
        List<Doctor> list= new ArrayList<>();
        String query = "SELECT * FROM doctors";
        Doctor doctor = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctor = new Doctor(rs.getInt("doctor_id"),
                        rs.getString("doctor_name"),
                        rs.getString("specialisation")
                );
                list.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return list;
    }
}
