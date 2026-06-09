package com.example.appointment_management_system.doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;

    @NotBlank(message = "Name can not be blank")
    private String doctorName;

    @NotBlank(message="Specialisation can not be blank")
    private String specialisation;

    public Doctor(){}

    public Doctor(String doctorName, String specialisation) {
        this.doctorName = doctorName;
        this.specialisation = specialisation;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }
}
