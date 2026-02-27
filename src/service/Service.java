package service;

import com.sun.source.tree.Tree;
import entities.Appointment;
import entities.Doctor;
import entities.Person;
import entities.User;
import repositries.*;
import validations.AppointmentValidation;
import validations.PersonValidation;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


public class Service {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentValidation appointmentValidation;
    private final PersonValidation personValidation;

    public Service(AppointmentRepository appointmentRepository,
                   DoctorRepository doctorRepository,
                   UserRepository userRepository,
                   AppointmentValidation appointmentValidation,
                   PersonValidation personValidation) {
        this.appointmentRepository=appointmentRepository;
        this.doctorRepository=doctorRepository;
        this.userRepository=userRepository;
        this.appointmentValidation=appointmentValidation;
        this.personValidation=personValidation;
    }

    public  Appointment.Status addAppointment(int doctorId, LocalDateTime dateTime, int patientId){
        User patient = userRepository.getUser(patientId);

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

    public boolean cancelAppointment(int doctorId, int patientId, LocalDateTime dateTime) {
        Doctor doctor = doctorRepository.getDoctor(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor does not exist.");
        }

        if(appointmentRepository.con)

        if(dateTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("Appointments can not be cancelled in past.");
        }
        String patientName=userRepository.getUser(patientId).getFullName();
        if(patientName ==null){
            throw new RuntimeException("No Appointment Exist");
        }
        Appointment appointment=new Appointment(doctorId,patientId,patientName,dateTime);
        return appointmentRepository.cancelAppointment(appointment);
    }

    public boolean reScheduleAppointment(int doctorId,int patientId,LocalDateTime oldDateTime,LocalDateTime newDateTime){
        TreeMap<LocalDateTime,Appointment> schedule=appointmentRepository.getAllAppointments().get(doctorId);
        if(schedule==null) {
            throw new RuntimeException("No appointment exist for doctor: "+doctorRepository.getDoctor(doctorId).getFullName());
        }
        if(!schedule.containsKey(oldDateTime)){
            throw new RuntimeException("No scheduled appointment at "+oldDateTime);
        }
        if(schedule.containsKey(newDateTime)){
            throw new RuntimeException("Slot already booked at "+newDateTime);
        }
        cancelAppointment(doctorId,patientId,oldDateTime);
        addAppointment(doctorId,newDateTime, patientId,userRepository.getUser(patientId).getFullName());
        return true;
    }

}

