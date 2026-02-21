package service;

import entities.Appointment;
import entities.Person;
import entities.User;
import repositries.*;
import validations.AppointmentValidation;
import validations.PersonValidation;

import java.time.LocalDateTime;
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
                   PersonValidation personValidation){
        this.appointmentRepository=appointmentRepository;
        this.doctorRepository=doctorRepository;
        this.userRepository=userRepository;
        this.appointmentValidation=appointmentValidation;
        this.personValidation=personValidation;
    }

    public Appointment.Status addAppointment(int doctorId, LocalDateTime dateTime, int patientId, String patient){
        personValidation.validate(new Person(patientId,patient));
        appointmentValidation.validate(new Appointment(doctorId,patientId,patient,dateTime));

        if(doctorRepository.getDoctor(doctorId)==null){
            throw new RuntimeException("Doctor Not Found");
        }

        if(dateTime.isAfter(LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDayOfMonth(),20,30))
        && dateTime.isBefore(LocalDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDayOfMonth(),10,0))){
            throw new RuntimeException("Working Hours: 10:00 am to 8:30 pm");
        }

        if(!appointmentRepository.isSlotAvailable(doctorId,dateTime)){
            throw new RuntimeException("Slot already booked");
        }

        Appointment appointment=new Appointment(doctorId,patientId,patient,dateTime);

        appointmentRepository.addAppointment(appointment);
        return appointment.getStatus();
    }

    public boolean cancelAppointment(int doctorId, int patientId, LocalDateTime dateTime) {
        if(dateTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("Appointments can not be cancelled in past.");
        }
        String patientName=userRepository.getUser(patientId).getFullName();
        Appointment appointment=new Appointment(doctorId,patientId,patientName,dateTime);
        boolean flag=appointmentRepository.cancelAppointment(appointment);
        return flag;
    }
}
