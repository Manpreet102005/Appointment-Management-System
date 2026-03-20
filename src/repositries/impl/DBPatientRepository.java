package repositries.impl;

import entities.Patient;
import repositries.PatientRepository;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBPatientRepository implements PatientRepository {
    public void addPatient(Patient patient){
        String query="INSERT INTO patients (id, fullname) VALUES (?,?)";
        try(Connection conn= DatabaseConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(query)){
            ps.setInt(1,patient.getId());
            ps.setString(2,patient.getFullName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
    }
    public List<Patient> getAllPatients() {
        List<Patient> list= new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                list.add(new Patient(rs.getInt("id"), rs.getString("fullname")));
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
            ResultSet rs = ps.executeQuery(query);
            if (rs.next()) {
                patient = new Patient(rs.getInt("id"), rs.getString("fullname"));
            }
        } catch (SQLException e) {
            System.out.println("State: " + e.getSQLState());
            System.out.println("Code : " + e.getErrorCode());
            System.out.println("Msg  : " + e.getMessage());
        }
        return patient;
    }
}
