import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import repositories.*;
import service.Service;
import repositories.impl.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class Main {
       public static void main(String[] args) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Scanner sc = new Scanner(System.in);

        System.out.println("Select Storage aMode:");
        System.out.println("1. Database");
        System.out.println("2. In-Memory");
        System.out.print("Enter choice: ");
        int mode = sc.nextInt();
        sc.nextLine();

        DoctorRepository doctorRepository;
        PatientRepository patientRepository;
        AppointmentRepository appointmentRepository;

        if(mode == 1) {
            doctorRepository= new DBDoctorRepository();
            patientRepository= new DBPatientRepository();
            appointmentRepository= new DBAppointmentRepository();
        } else {
            doctorRepository= new InMemoryDoctorRepository();
            patientRepository= new InMemoryPatientRepository();
            appointmentRepository =new InMemoryAppointmentRepository();
        }

        Service service=new Service(appointmentRepository,doctorRepository,patientRepository);

        boolean running = true;
        while (running) {
            System.out.println("\n===== HOSPITAL MANAGEMENT MENU =====");
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Patient");
            System.out.println("3. Book Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. View All Appointments");
            System.out.println("7. View All Patients");
            System.out.println("8. View All Doctors");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter Doctor ID: ");
                        int dId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Doctor Name: ");
                        String dName = sc.nextLine();

                        System.out.print("Enter Specialization: ");
                        String dSpec = sc.nextLine();

                        boolean dAddStatus=service.addDoctor(dId, dName, dSpec);
                        if(dAddStatus) System.out.println("Doctor Added successfully!");
                        else System.out.println("Doctor with this ID already exists!");
                        break;

                    case 2:
                        System.out.print("Enter Patient ID: ");
                        int pId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient Name: ");
                        String pName = sc.nextLine();

                        boolean pAddStatus=service.addPatient(pId, pName);
                        if(pAddStatus) System.out.println("Patient added successfully!");
                        else System.out.println("Patient with this ID already exists!");
                        break;

                    case 3:
                        System.out.print("Enter Doctor ID: ");
                        int bookDocId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient ID: ");
                        int bookPatId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Appointment Time (yyyy-MM-dd HH:mm): ");
                        String appDateTimeString = sc.nextLine();

                        LocalDateTime appDateTime;
                        try {
                            appDateTime = LocalDateTime.parse(appDateTimeString, formatter);
                        } catch (DateTimeParseException e) {
                            throw new IllegalStateException("Invalid date-time format. Please use yyyy-MM-dd HH:mm");
                        }

                        Appointment.Status bookingStatus = service.addAppointment(bookDocId, appDateTime, bookPatId);

                        if (bookingStatus.equals(Appointment.Status.BOOKED))
                            System.out.println("Appointment booked successfully!");
                        else System.out.println("Appointment Booking Failed. Try Again");
                        break;

                    case 4:
                        System.out.print("Enter Doctor ID: ");
                        int cancelDocId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient ID: ");
                        int cancelPatId = sc.nextInt();
                        sc.nextLine();

                        Appointment.Status cancellationStatus = service.cancelAppointment(cancelDocId, cancelPatId);
                        if (cancellationStatus.equals(Appointment.Status.CANCELLED))
                            System.out.println("Appointment Cancelled Successfully!");
                        else System.out.println("Appointment Cancellation Failed. Try Again");
                        break;

                    case 5:
                        System.out.print("Enter Doctor ID: ");
                        int docId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient ID: ");
                        int patId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter New Time (yyyy-MM-dd HH:mm): ");
                        String newDateTimeString = sc.nextLine();

                        LocalDateTime newDateTime;
                        try {
                            newDateTime = LocalDateTime.parse(newDateTimeString, formatter);
                        } catch (DateTimeParseException e) {
                            throw new IllegalStateException("Invalid date-time format. Please use yyyy-MM-dd HH:mm");
                        }

                        Appointment existing = appointmentRepository.getPatientAppointment(docId,patId);
                        if(existing==null){
                            System.out.println("No Appointment Found");
                            break;
                        }

                        boolean status = service.reScheduleAppointment(docId, patId, existing.getDateTime(), newDateTime);
                        if (status) System.out.println("Appointment Rescheduled Successfully");
                        else System.out.println("Rescheduling Failed. Try Again");
                        break;

                    case 6:
                        List<Appointment> allAppointments = appointmentRepository.getAllAppointments();
                        allAppointments.forEach(System.out::println);
                        break;

                    case 7:
                        List<Patient> allPatients = patientRepository.getAllPatients();
                        allPatients.forEach(System.out::println);
                        break;

                    case 8:
                        List<Doctor> allDoctors = doctorRepository.getAllDoctors();
                        allDoctors.forEach(System.out::println);
                        break;

                    case 0:
                        running = false;
                        System.out.println("Exiting system...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Error: "+ e.getMessage());
            }
        }
        sc.close();
    }
}