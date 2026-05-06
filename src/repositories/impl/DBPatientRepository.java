package repositories.impl;

import entities.Patient;
import repositories.PatientRepository;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBPatientRepository implements PatientRepository {
    public Patient addPatient(Patient patient){
        String query="INSERT INTO patients (patient_name,phone_no,gender,blood_group) VALUES (?,?,?,?)";
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,patient.getFullName());
            ps.setString(2,patient.getPhoneNo());
            ps.setString(3, String.valueOf(patient.getGender()));
            ps.setString(4,patient.getBloodGroup());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                patient.setId(rs.getInt(1));
                return patient;
            }
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return null;
    }
    public List<Patient> getAllPatients() {
        List<Patient> list= new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Patient(rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("phone_no"),
                        (rs.getString("gender")).charAt(0),
                        rs.getString("blood_group")));
            }
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return list;
    }

    public Patient getPatient(int id) {
        String query = "SELECT * FROM patients WHERE id=?";
        Patient patient = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                patient = new Patient(rs.getInt("id"), 
                        rs.getString("patient_name"),
                        rs.getString("phone_no"),
                        (rs.getString("gender")).charAt(0),
                        rs.getString("blood_group"));
            }
        } catch (SQLException e) {
            System.err.println("State: " + e.getSQLState());
            System.err.println("Code : " + e.getErrorCode());
            System.err.println("Msg  : " + e.getMessage());
        }
        return patient;
    }
    public boolean removePatient(int id){
        String query= "DELETE FROM patients WHERE id=?";
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
}
