package service;

import entities.Appointment;
import entities.Doctor;
import entities.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import repositories.impl.InMemoryAppointmentRepository;
import repositories.impl.InMemoryDoctorRepository;
import repositories.impl.InMemoryPatientRepository;

import java.time.LocalDateTime;

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

    private Doctor newDoctor(){
        return service.addDoctor("Dummy Doctor","Dummy Specialization");
    }

    private Patient newPatient(){
        return service.addPatient("Dummy Patient", "1234567890",'M',"O+");
    }

    //Tests

    @Nested
    class PatientTests{
        @Test
        void addPatientValid(){
            Patient p=newPatient();
            assertNotNull(p);
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
        @Test
        void addDoctorValid(){
            Doctor d= newDoctor();
            assertNotNull(d);
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
    class AppointmentTests{

        private LocalDateTime slot(int day, int hour, int minute) {
            return LocalDateTime.of(LocalDateTime.now().getYear(),
                    LocalDateTime.now().getMonthValue(),
                    day, hour, minute);
        }

        @Test
        void addAppointmentValid(){
            Doctor d=newDoctor();
            Patient p=newPatient();
            LocalDateTime slot=slot(LocalDateTime.now().getDayOfMonth()+1 ,12,30);
            Appointment a=service.addAppointment(d.getId(),slot,p.getId());

            assertNotNull(a);
            assertSame(a,appointmentRepo.getAppointmentById(d.getId(),a.getAppointmentId()));
            assertTrue(a.getAppointmentId()>0);
            assertEquals(p.getFullName(), a.getPatientName());
            assertEquals(slot,a.getDateTime());
            assertEquals(Appointment.Status.BOOKED,a.getStatus());
            assertEquals(1,appointmentRepo.getAllAppointments().size());
        }

        /*@Test
        void addAppointmentPatientNotRegistered(){
            Patient p= new Patient(1,"Patient");
            Doctor d=newDoctor();
            Appointment a=service.addAppointment(d.getId(),slot())
        }*/
    }
}
