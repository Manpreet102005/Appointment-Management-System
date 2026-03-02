import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import repositries.*;
import service.Service;
import repositries.impl.*;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Scanner sc = new Scanner(System.in);
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

                        service.addDoctor(dId, dName, dSpec);
                        System.out.println("Doctor Added successfully!");
                        break;

                    case 2:
                        System.out.print("Enter Patient ID: ");
                        int pId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Patient Name: ");
                        String pName = sc.nextLine();

                        service.addPatient(pId, pName);
                        System.out.println("Patient added successfully!");
                        break;

                    case 3:
                        System.out.print("Enter Doctor ID: ");
                        int bookDocId = sc.nextInt();

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

                        System.out.print("Enter Patient ID: ");
                        int cancelPatId = sc.nextInt();
                        sc.nextLine();

                        Appointment.Status cancellationStatus = service.cancelAppointment(cancelDocId, cancelPatId);
                        if (cancellationStatus.equals(Appointment.Status.CANCELLED)) System.out.println("Appointment cancelled successfully!");
                        else System.out.println("Appointment Cancellation Failed. Try Again");
                        break;

                    case 5:
                        System.out.print("Enter Doctor ID: ");
                        int docId = sc.nextInt();

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

                        boolean status = service.reScheduleAppointment(docId,
                                patId,
                                appointmentRepository.getPatientAppointment(docId, patId).getDateTime(),
                                newDateTime);
                        if (status) System.out.println("Appointment Rescheduled Successfully");
                        else System.out.println("Appointment Reschedulation Failed. Try Again");

                    case 6:
                        ConcurrentHashMap<Integer, TreeMap<Integer, Appointment>> allAppointments = appointmentRepository.getAllAppointments();
                        for(int key:allAppointments.keySet()){
                            System.out.println("Doctor Details:");
                            System.out.println("_______________");
                            System.out.printf("ID: %s Name: %s | Specialization: %s",
                                    key,
                                    doctorRepository.getDoctor(key).getFullName(),
                                    doctorRepository.getDoctor(key).getSpecialization()
                            );

                            System.out.print("Patient ID  |  Patient Name  |  Appointment Time  |  Status");
                            TreeMap<Integer,Appointment> schedule=allAppointments.get(key);
                            for (int id:schedule.keySet()){
                                Appointment appointment= schedule.get(id);
                                System.out.printf("%-10d  |  %-10s  |  %-10s  |  %-10s",
                                        id,
                                        appointment.getPatientName(),
                                        appointment.getDateTime().format(formatter),
                                        appointment.getStatus()
                                );
                            }
                        }
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