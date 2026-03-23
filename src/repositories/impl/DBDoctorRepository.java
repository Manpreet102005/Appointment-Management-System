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
        String query="INSERT INTO doctors (id, fullname,specialisation) VALUES (?,?,?)";
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,doctor.getId());
            ps.setString(2,doctor.getFullName());
            ps.setString(3,doctor.getSpecialization());
            int rows=ps.executeUpdate();
            return rows==1;
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
            return false;
        }
    }
    public boolean removeDoctor(int id){
        String query= "DELETE FROM doctors WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1,id);
            int rows=ps.executeUpdate();
            return rows==1;
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
            return false;
        }
    }
    public Doctor getDoctor(int id){
        String query = "SELECT * FROM doctors WHERE id=?";
        Doctor doctor = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(rs.getInt("id"),
                        rs.getString("fullname"),
                        rs.getString("specialisation")
                );
            }
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
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
                doctor = new Doctor(rs.getInt("id"),
                        rs.getString("fullname"),
                        rs.getString("specialisation")
                );
                list.add(doctor);
            }
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
        return list;
    }
}
