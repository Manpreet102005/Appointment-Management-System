package service;

import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import repositories.*;
import validations.PersonValidation;
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
    public Doctor addDoctor(String fullName, String specialization){
        PersonValidation.validateName(fullName);
        return doctorRepository.addDoctor(new Doctor(fullName, specialization));
    }

    public Patient addPatient(String fullName,String phoneNo,char gender, String bloodGroup){
        PersonValidation.validateName(fullName);
        PersonValidation.validatePhoneNo(phoneNo);
        PersonValidation.validateGender(gender);
        PersonValidation.validateBloodGroup(bloodGroup);
        return patientRepository.addPatient(new Patient(fullName,phoneNo,gender,bloodGroup));
    }
    public Appointment addAppointment(int doctorId, LocalDateTime dateTime, int patientId){
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

        if(!appointmentRepository.isSlotAvailable(doctorId,dateTime)){
            throw new IllegalStateException("Slot already booked");
        }

        Appointment appointment=new Appointment(doctorId,patientId, patient.getFullName(), dateTime);

        appointmentRepository.addAppointment(appointment);
        return appointment;
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

    public Appointment reScheduleAppointment(int doctorId,int appointmentId,LocalDateTime newDateTime){
        Doctor doctor=doctorRepository.getDoctor(doctorId);

        if(doctor==null) {
            throw new NoSuchElementException("Doctor Not Found");
        }

        Appointment appointment =appointmentRepository.getAppointmentById(doctorId,appointmentId);
        if (appointment==null) {
            throw new NoSuchElementException("No scheduled appointment");
        }

        if(newDateTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Appointment can't be rescheduled to past");
        }
        LocalDateTime start = LocalDateTime.of(newDateTime.getYear(),newDateTime.getMonth(),newDateTime.getDayOfMonth(),10, 0);
        LocalDateTime end = LocalDateTime.of(newDateTime.getYear(),newDateTime.getMonth(),newDateTime.getDayOfMonth(),20, 30);

        if (newDateTime.isBefore(start)|| newDateTime.isAfter(end)) {
            throw new IllegalArgumentException("Working Hours: 10:00 am to 8:30 pm");
        }

        if(!appointmentRepository.isSlotAvailable(doctorId,newDateTime)){
            throw new IllegalStateException("Slot already booked");
        }

        Appointment rescheduledAppointment=appointmentRepository.rescheduleAppointment(doctorId,appointmentId,newDateTime);
        return rescheduledAppointment;
    }
}

