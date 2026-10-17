package service;

import entities.Doctor;
import entities.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import repositories.impl.InMemoryAppointmentRepository;
import repositories.impl.InMemoryDoctorRepository;
import repositories.impl.InMemoryPatientRepository;

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
}
