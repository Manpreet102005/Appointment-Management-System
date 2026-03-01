package service;

import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import entities.Person;
import repositries.*;
import validations.PersonValidation;

import java.time.LocalDateTime;


public class Service {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public Service(AppointmentRepository appointmentRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository) {
        this.appointmentRepository=appointmentRepository;
        this.doctorRepository=doctorRepository;
        this.patientRepository=patientRepository;
    }
    public void addDoctor(int doctorId, String fullName, String specialization){
        PersonValidation.validate(new Person(doctorId,fullName));
        doctorRepository.addDoctor(new Doctor(doctorId,fullName,specialization));
    }
    public  Appointment.Status addAppointment(int doctorId, LocalDateTime dateTime, int patientId){
        Patient patient = patientRepository.getPatient(patientId);

        if (patient == null) {
            throw new RuntimeException("Patient not registered.");
        }

        if(doctorRepository.getDoctor(doctorId)==null){
            throw new RuntimeException("Doctor Not Found");
        }

        LocalDateTime start = LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDayOfMonth(),10, 0);
        LocalDateTime end = LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDayOfMonth(),20, 30);

        if (dateTime.isBefore(start)|| dateTime.isAfter(end)) {
            throw new RuntimeException("Working Hours: 10:00 am to 8:30 pm");
        }

        if(!appointmentRepository.isSlotAvailable(doctorId,dateTime)){
            throw new RuntimeException("Slot already booked");
        }

        Appointment appointment=new Appointment(doctorId,patientId, patient.getFullName(), dateTime);

        appointmentRepository.addAppointment(appointment);
        return appointment.getStatus();
    }

    public boolean cancelAppointment(int doctorId, int patientId) {
        Doctor doctor = doctorRepository.getDoctor(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor does not exist.");
        }

        if(!appointmentRepository.getAllAppointments().containsKey(doctorId)){
            throw new RuntimeException("Doctor is not available");
        }
        LocalDateTime dateTime=appointmentRepository.getAllAppointmentsOf(doctorId).get(patientId).getDateTime();
        if (!appointmentRepository.appointmentExists(doctorId, patientId, dateTime)){
            throw new RuntimeException("No Appointment exist with these details");
        }

        return appointmentRepository.cancelAppointment(doctorId,patientId);
    }

    public boolean reScheduleAppointment(int doctorId,int patientId,LocalDateTime oldDateTime,LocalDateTime newDateTime){
        Doctor doctor=doctorRepository.getDoctor(doctorId);

        if(doctor==null) {
            throw new RuntimeException("Doctor Not Found");
        }
        if (!appointmentRepository.appointmentExists(doctorId,  patientId, oldDateTime)) {
            throw new RuntimeException("No scheduled appointment at " + oldDateTime);
        }

        if (!appointmentRepository.isSlotAvailable(doctorId, newDateTime)) {
            throw new RuntimeException("Slot already booked at " + newDateTime);
        }
        cancelAppointment(doctorId,patientId);
        addAppointment(doctorId,newDateTime, patientId);
        return true;
    }
}

