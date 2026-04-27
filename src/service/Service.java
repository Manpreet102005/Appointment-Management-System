package service;

import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import entities.Person;
import repositories.*;
import validations.PersonValidation;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;


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
    public boolean addDoctor(int doctorId, String fullName, String specialization){
        PersonValidation.validate(new Person(doctorId,fullName));
        return doctorRepository.addDoctor(new Doctor(doctorId,fullName,specialization));
    }

    public boolean addPatient(int patientId, String fullName){
        PersonValidation.validate(new Person(patientId,fullName));
        return patientRepository.addPatient(new Patient(patientId,fullName));
    }
    public Appointment.Status addAppointment(int doctorId, LocalDateTime dateTime, int patientId){
        Patient patient = patientRepository.getPatient(patientId);

        if (patient == null) {
            throw new NoSuchElementException("Patient not registered.");
        }

        if(doctorRepository.getDoctor(doctorId)==null){
            throw new NoSuchElementException("Doctor Not Found");
        }

        if(dateTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Appointment can't be made in past");
        }
        LocalDateTime start = LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDayOfMonth(),10, 0);
        LocalDateTime end = LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDayOfMonth(),20, 30);

        if (dateTime.isBefore(start)|| dateTime.isAfter(end)) {
            throw new IllegalArgumentException("Working Hours: 10:00 am to 8:30 pm");
        }

        if (appointmentRepository.hasAppointmentOnDay(doctorId, patientId, dateTime.toLocalDate())) {
            throw new IllegalStateException("Patient already has an appointment with this doctor on this day");
        }

        if(!appointmentRepository.isSlotAvailable(doctorId,dateTime)){
            throw new IllegalStateException("Slot already booked");
        }

        Appointment appointment=new Appointment(doctorId,patientId, patient.getFullName(), dateTime);

        appointmentRepository.addAppointment(appointment);
        return appointment.getStatus();
    }

    public Appointment.Status cancelAppointment(int doctorId, int appointmentId) {
        Doctor doctor = doctorRepository.getDoctor(doctorId);
        if (doctor == null) {
            throw new NoSuchElementException("Doctor does not exist.");
        }
        Appointment patientAppointment=appointmentRepository.getAppointmentById(doctorId,appointmentId);

        if(patientAppointment==null){
            throw new NoSuchElementException("Appointment not found");
        }

        return appointmentRepository.cancelAppointment(doctorId,appointmentId);
    }

    public Appointment reScheduleAppointment(int doctorId,int patientId,int appointmentId,LocalDateTime newDateTime){
        Doctor doctor=doctorRepository.getDoctor(doctorId);

        if(doctor==null) {
            throw new NoSuchElementException("Doctor Not Found");
        }
        Appointment oldAppointment =appointmentRepository.getAppointmentById(doctorId,appointmentId);
        if (oldAppointment==null) {
            throw new NoSuchElementException("No scheduled appointment");
        }

        if(oldAppointment.getPatientId()!=patientId){
            throw new IllegalArgumentException("Patient mismatch");
        }

        if (oldAppointment.getDateTime().equals(newDateTime)) {
            throw new IllegalArgumentException("New time cannot be same as old time");
        }

        if (!appointmentRepository.isSlotAvailable(doctorId, newDateTime)) {
            throw new IllegalStateException("Slot already booked at " + newDateTime);
        }
        Appointment newAppointment;
        try {
            appointmentRepository.cancelAppointment(doctorId, appointmentId);

            newAppointment = new Appointment(doctorId, patientId, oldAppointment.getPatientName(),
                    newDateTime);
            appointmentRepository.addAppointment(newAppointment);
        }
        catch(Exception e){
            throw new RuntimeException("Rescheduling Failed. Try Again");
        }
        return newAppointment;
    }
}

