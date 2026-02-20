package service;

import entities.Appointment;
import entities.Person;
import repositries.AppointmentRepository;
import repositries.DoctorRepository;
import repositries.UserRepository;
import validations.AppointmentValidation;
import validations.PersonValidation;

import java.time.LocalDateTime;

public class Service {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentValidation appointmentValidation;
    private final PersonValidation personValidation;

    public Service(AppointmentRepository appointmentRepository,
                   DoctorRepository doctorRepository,
                   UserRepository userRepository,
                   AppointmentValidation appointmentValidation,
                   PersonValidation personValidation){
        this.appointmentRepository=appointmentRepository;
        this.doctorRepository=doctorRepository;
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
}
