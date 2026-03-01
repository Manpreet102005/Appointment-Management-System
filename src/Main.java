import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import repositries.*;
import service.Service;
import repositries.impl.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static DoctorRepository doctorRepository = new InMemoryDoctorRepository();
    private static PatientRepository patientRepository = new InMemoryPatientRepository();
    private static AppointmentRepository appointmentRepository = new InMemoryAppointmentRepository();
    private static Service service = new Service(appointmentRepository, doctorRepository, patientRepository);

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n===== HOSPITAL MANAGEMENT MENU =====");
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Patient");
            System.out.println("3. Book Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. View All Appointments");
            System.out.println("6. View All Patients");
            System.out.println("7. View All Doctors");
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

                        service.addDoctor(dId,dName,dSpec);
                        break;

                    case 2:
                        System.out.print("Enter Patient ID: ");
                        int pId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient Name: ");
                        String pName = sc.nextLine();

                        patientRepository.addPatient(new Patient(pId, pName));
                        System.out.println("Patient added successfully!");
                        break;

                    case 3:
                        System.out.print("Enter Doctor ID: ");
                        int bookDocId = sc.nextInt();

                        System.out.print("Enter Patient ID: ");
                        int bookPatId = sc.nextInt();

                        service.addAppointment(bookDocId, LocalDateTime.now(),bookPatId);
                        System.out.println("Appointment booked successfully!");
                        break;

                    case 4:
                        System.out.print("Enter Doctor ID: ");
                        int cancelDocId = sc.nextInt();

                        System.out.print("Enter Patient ID: ");
                        int cancelPatId = sc.nextInt();

                        service.cancelAppointment(cancelDocId, cancelPatId);
                        System.out.println("Appointment cancelled successfully!");
                        break;

                    case 5:
                        ConcurrentHashMap<Integer, TreeMap<Integer, Appointment>> allAppointments = appointmentRepository.getAllAppointments();
                        allAppointments.values().forEach(System.out::println);
                        break;

                    case 6:
                        List<Patient> allPatients = patientRepository.getAllPatients();
                        allPatients.forEach(System.out::println);
                        break;

                    case 7:
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