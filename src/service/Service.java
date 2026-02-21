package service;

import com.sun.source.tree.Tree;
import entities.Appointment;
import entities.Doctor;
import entities.Person;
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

    public  Appointment.Status addAppointment(int doctorId, LocalDateTime dateTime, int patientId, String patient){
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
    public void allAppointments(){
        ConcurrentHashMap<Integer, TreeMap<LocalDateTime,Appointment>> allAppointments;
        allAppointments=appointmentRepository.getAllAppointments();

        if (allAppointments==null || allAppointments.isEmpty()) {
            System.out.println("No appointments scheduled in the system.");
            return;
        }
        for(int doctorID: allAppointments.keySet()){
            Doctor doctor=doctorRepository.getDoctor(doctorID);
            System.out.println("Doctor Details");
            System.out.println("--------------");
            System.out.println("ID: "+doctorID);
            System.out.println("Name: "+doctor.getFullName());
            System.out.println("Specialization: "+doctor.getSpecialization());
            System.out.println();
            TreeMap<LocalDateTime,Appointment> schedule=allAppointments.get(doctorID);

            if (schedule == null || schedule.isEmpty()) {
                System.out.println("No appointments for this doctor.");
                continue;
            }
            System.out.println("Appointments");
            System.out.println("------------");
            System.out.printf("%-20s %-12s %-20s %-10s%n","Date & Time", "Patient ID", "Patient Name", "Status");
            System.out.println("--------------------------------------------------------------------------");

            for (Appointment appointment : schedule.values()) {
                System.out.printf("%-20s %-12d %-20s %-10s%n",
                        appointment.getDateTime(),
                        appointment.getPatientId(),
                        appointment.getPatientName(),
                        appointment.getStatus()
                );
            }
        }
    }
}
