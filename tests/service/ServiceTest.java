package service;

import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repositories.impl.InMemoryAppointmentRepository;
import repositories.impl.InMemoryDoctorRepository;
import repositories.impl.InMemoryPatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private InMemoryAppointmentRepository appointmentRepo;
    private InMemoryDoctorRepository doctorRepo;
    private InMemoryPatientRepository patientRepo;
    private Service service;

    @BeforeEach
    void setup(){
        appointmentRepo=new InMemoryAppointmentRepository();
        doctorRepo=new InMemoryDoctorRepository();
        patientRepo=new InMemoryPatientRepository();
        service=new Service(appointmentRepo,doctorRepo,patientRepo);
    }
    //Helpers

    private LocalDateTime slot(LocalDate date, int hour, int minute) {
        return date.atTime(hour,minute);
    }

    private Doctor newDoctor(){
        return service.addDoctor("Dummy Doctor","Dummy Specialization");
    }

    private Patient newPatient(){
        return service.addPatient("Dummy Patient", "1234567890",'M',"O+");
    }

    private Appointment newAppointment() {
        Doctor d = newDoctor();
        Patient p = newPatient();
        return service.addAppointment(
                d.getId(),
                slot(LocalDate.now().plusDays(1), 12, 30),
                p.getId()
        );
    }
    //Tests

    @Nested
    class PatientTests{
        @Test
        void addPatientValid(){
            Patient p=newPatient();
            assertEquals("Dummy Patient",p.getFullName());
            assertSame(p,patientRepo.getPatient(p.getId()));
            assertEquals(1,patientRepo.getAllPatients().size());
        }

        @Test
        void addPatientInvalid(){
            assertAll(
                    ()->assertThrows(IllegalArgumentException.class,
                            ()-> service.addPatient(
                                    "Pat*ient",//invalid name
                                    "1234567890",
                                    'F',
                                    "B+")),
                    ()->assertThrows(IllegalArgumentException.class,
                            ()->service.addPatient(
                                    "Patient",
                                    "12345678",//invalid phone no
                                    'F',
                                    "B+"
                            )),
                    ()->assertThrows(IllegalArgumentException.class,
                            ()->service.addPatient(
                                    "Patient",
                                    "1234567890",
                                    'H',//invalid gender
                                    "B+"
                            )),

                    ()->assertThrows(IllegalArgumentException.class,
                            ()->service.addPatient(
                                    "Patient",
                                    "1234567890",
                                    'H',
                                    "B+-"//invalid blood group
                            ))

            );
            assertTrue(patientRepo.getAllPatients().isEmpty());
        }

    }

    @Nested
    class DoctorTests{
        @Nested
        class AddDoctorTests{
            @Test
            void addDoctorValid(){
                Doctor d= newDoctor();
                assertEquals("Dummy Doctor",d.getFullName());
                assertSame(d,doctorRepo.getDoctor(d.getId()));
                assertEquals(1,doctorRepo.getAllDoctors().size());
            }

            @Test
            void addDoctorInvalid(){
                assertThrows(IllegalArgumentException.class,
                        ()->service.addDoctor("D@ctor Dummy!","Physician"));
                assertTrue(doctorRepo.getAllDoctors().isEmpty());
            }
        }

        @Nested
        class RemoveDoctorTests {
            @Test
            void removeDoctorValid() {
                Doctor d = newDoctor();
                Patient p1 = newPatient();
                Patient p2 = newPatient();
                Appointment a1 = new Appointment(d.getId(),
                        p1.getId(),
                        p1.getFullName(),
                        slot(LocalDate.now().plusDays(1), 14, 30)
                );
                Appointment a2 = new Appointment(d.getId(),
                        p2.getId(),
                        p2.getFullName(),
                        slot(LocalDate.now().plusDays(1), 15, 23)
                );
                appointmentRepo.addAppointment(a1);
                appointmentRepo.addAppointment(a2);
                List<Appointment> doctorAppointments = new ArrayList<>();
                for (Appointment a : appointmentRepo.getAllAppointments()) {
                    if (a.getDoctorId() == d.getId()) {
                        doctorAppointments.add(a);
                    }
                }
                assertEquals(2, doctorAppointments.size());
                assertTrue(service.removeDoctor(d.getId()));
                assertTrue(doctorRepo.getAllDoctors().isEmpty());

                for (Appointment a : doctorAppointments) {
                    assertEquals(Appointment.Status.CANCELLED, a.getStatus());
                }

            }

            @ParameterizedTest
            @ValueSource(ints = {0,-1,-225,Integer.MIN_VALUE})
            void removeDoctorInvalidId(int invalidId){
                newDoctor();
                assertEquals("Id can't be smaller than 1",
                        assertThrows(IllegalArgumentException.class,
                                ()-> service.removeDoctor(invalidId)
                        ).getMessage()
                );
                assertEquals(1,doctorRepo.getAllDoctors().size());
            }

            @Test
            void removeDoctorDoctorNotFound(){
                assertEquals("Doctor Not Found",
                        assertThrows(NoSuchElementException.class,
                                ()->service.removeDoctor(1)
                        ).getMessage()
                );
                assertTrue(doctorRepo.getAllDoctors().isEmpty());
            }
        }

    }

    @Nested
    class AppointmentTests{

        @Nested
        class AddAppointmentTests {
            @Test
            void addAppointmentValid() {
                Doctor d = newDoctor();
                Patient p = newPatient();
                LocalDateTime slot = slot(LocalDate.now().plusDays(1), 12, 30);
                Appointment a = service.addAppointment(d.getId(), slot, p.getId());

                assertNotNull(a);
                assertSame(a, appointmentRepo.getAppointmentById(d.getId(), a.getAppointmentId()));
                assertTrue(a.getAppointmentId() > 0);
                assertEquals(p.getFullName(), a.getPatientName());
                assertEquals(slot, a.getDateTime());
                assertEquals(Appointment.Status.BOOKED, a.getStatus());
                assertEquals(1, appointmentRepo.getAllAppointments().size());
            }

            @Test
            void addAppointmentPatientNotRegistered() {
                Patient p = new Patient(1, "Patient");
                Doctor d = newDoctor();
                NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                        () -> service.addAppointment(d.getId(),
                                slot(LocalDate.now().plusDays(1), 12, 45),
                                p.getId()));
                assertEquals("Patient not registered.", ex.getMessage());
                assertTrue(appointmentRepo.getAllAppointments().isEmpty());
            }

            @Test
            void addAppointmentDoctorNotFound() {
                Patient p = newPatient();
                Doctor d = new Doctor("Doctor", "Physician");

                NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                        () -> service.addAppointment(
                                d.getId(),
                                slot(LocalDate.now().plusDays(1), 1, 30),
                                p.getId())
                );
                assertEquals("Doctor Not Found", ex.getMessage());
                assertTrue(appointmentRepo.getAllAppointments().isEmpty());
            }

            @Test
            void addAppointmentInPast() {
                Doctor d = newDoctor();
                Patient p = newPatient();

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                        () -> service.addAppointment(d.getId(),
                                slot(LocalDate.now().minusDays(1), 12, 30),
                                p.getId()));
                assertEquals("Appointment can't be made in past", ex.getMessage());
                assertTrue(appointmentRepo.getAllAppointments().isEmpty());
            }

            @Test
            void addAppointmentOutsideWorkingHours() {
                Doctor d = newDoctor();
                Patient p = newPatient();
                assertAll(
                        () -> assertEquals("Working Hours: 10:00 am to 8:30 pm",
                                assertThrows(IllegalArgumentException.class,
                                        () -> service.addAppointment(
                                                d.getId(),
                                                slot(LocalDate.now().plusDays(1), 9, 59),
                                                p.getId()
                                        )).getMessage())
                        ,
                        () -> assertEquals("Working Hours: 10:00 am to 8:30 pm",
                                assertThrows(IllegalArgumentException.class,
                                        () -> service.addAppointment(
                                                d.getId(),
                                                slot(LocalDate.now().plusDays(1), 20, 31),
                                                p.getId()
                                        )).getMessage())
                );
                assertTrue(appointmentRepo.getAllAppointments().isEmpty());
            }

            @Test
            void addAppointmentSlotAlreadyBooked() {
                Doctor d = newDoctor();
                Patient p1 = newPatient();
                Patient p2 = newPatient();
                LocalDateTime slot = slot(LocalDate.now().plusDays(1), 11, 30);
                Appointment a = service.addAppointment(d.getId(), slot, p1.getId());

                IllegalStateException ex = assertThrows(IllegalStateException.class,
                        () -> service.addAppointment(d.getId(), slot, p2.getId()));
                assertEquals("Slot already booked", ex.getMessage());
                assertEquals(1, appointmentRepo.getAllAppointments().size());
                assertSame(a, appointmentRepo.getAllAppointments().get(0));
            }
        }

        @Nested
        class CancelAppointmentTests {
            @Test
            void cancelAppointmentValid() {
                Appointment a = newAppointment();
                Appointment.Status status = service.cancelAppointment(a.getDoctorId(), a.getAppointmentId());
                assertEquals(Appointment.Status.CANCELLED, status);
                assertTrue(appointmentRepo.getAllAppointments().isEmpty());
            }

            @Test
            void cancelAppointmentDoctorDoesNotExist() {
                NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                        () -> service.cancelAppointment(Integer.MAX_VALUE, 1));//dummy data
                assertEquals("Doctor does not exist.", ex.getMessage());
            }

            @Test
            void cancelAppointmentAppointmentNotFound() {
                Appointment existingApp=newAppointment();
                NoSuchElementException ex=assertThrows(NoSuchElementException.class,
                        ()->service.cancelAppointment(existingApp.getDoctorId(),Integer.MAX_VALUE));
                assertEquals("Appointment not found",ex.getMessage());
            }
        }

        @Nested
        class RescheduleAppointmentTests{
            @Test
            void rescheduleAppointment(){
                Appointment existingApp=newAppointment();
                LocalDateTime newSlot=slot(LocalDate.now().plusDays(1),13,30);
                service.reScheduleAppointment(existingApp.getDoctorId(),existingApp.getAppointmentId(),newSlot);
                assertEquals(newSlot,existingApp.getDateTime());
                assertSame(existingApp,appointmentRepo.getAppointmentById(existingApp.getDoctorId(),existingApp.getAppointmentId()));
                assertEquals(1,appointmentRepo.getAllAppointments().size());
            }

            @Test
            void rescheduleAppointmentDoctorDoesNotExist() {
                NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                        () -> service.reScheduleAppointment(Integer.MAX_VALUE,Integer.MAX_VALUE,slot(LocalDate.now(),13,45)));//dummy data
                assertEquals("Doctor Not Found", ex.getMessage());
            }

            @Test
            void rescheduleAppointmentAppointmentNotFound() {
                Doctor d=newDoctor();
                NoSuchElementException ex=assertThrows(NoSuchElementException.class,
                        ()->service.reScheduleAppointment(d.getId(),Integer.MAX_VALUE,slot(LocalDate.now(),13,45)));
                assertEquals("No scheduled appointment",ex.getMessage());
            }

            @Test
            void rescheduleAppointmentInPast() {
                Appointment a=newAppointment();

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                        () -> service.reScheduleAppointment(a.getDoctorId(),a.getAppointmentId(),a.getDateTime().minusDays(1)));
                assertEquals("Appointment can't be rescheduled to past", ex.getMessage());
            }

            @Test
            void rescheduleAppointmentOutsideWorkingHours() {
                Appointment a=newAppointment();
                assertAll(
                        () -> assertEquals("Working Hours: 10:00 am to 8:30 pm",
                                assertThrows(IllegalArgumentException.class,
                                        () -> service.reScheduleAppointment(
                                                a.getDoctorId(),
                                                a.getAppointmentId(),
                                                slot(LocalDate.now().plusDays(1), 9, 59)
                                        )).getMessage())
                        ,
                        () -> assertEquals("Working Hours: 10:00 am to 8:30 pm",
                                assertThrows(IllegalArgumentException.class,
                                        () -> service.reScheduleAppointment(
                                                a.getDoctorId(),
                                                a.getAppointmentId(),
                                                slot(LocalDate.now().plusDays(1),20,31)
                                        )).getMessage())
                );
                assertEquals(1,appointmentRepo.getAllAppointments().size());
            }

            @Test
            void rescheduleAppointmentSlotAlreadyBooked() {
                Appointment existingApp=newAppointment();

                IllegalStateException ex = assertThrows(IllegalStateException.class,
                        () -> service.reScheduleAppointment(existingApp.getDoctorId(),existingApp.getAppointmentId(),existingApp.getDateTime()));
                assertEquals("Slot already booked", ex.getMessage());
                assertSame(existingApp, appointmentRepo.getAllAppointments().get(0));
            }
        }
    }
}
