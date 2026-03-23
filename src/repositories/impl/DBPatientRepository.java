package repositories.impl;

import entities.Patient;
import repositories.PatientRepository;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBPatientRepository implements PatientRepository {
    public boolean addPatient(Patient patient){
        String query="INSERT INTO patients (id, patient_name) VALUES (?,?)";
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,patient.getId());
            ps.setString(2,patient.getFullName());
            int rows=ps.executeUpdate();
            return rows==1;
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
            return false;
        }
    }
    public List<Patient> getAllPatients() {
        List<Patient> list= new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Patient(rs.getInt("id"), rs.getString("patient_name")));
            }
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
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
                patient = new Patient(rs.getInt("id"), rs.getString("patient_name"));
            }
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
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
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
            return false;
        }
    }
}
