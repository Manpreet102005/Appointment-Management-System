package validations;

import entities.Appointment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentValidationTest {

    private boolean runValidation(Appointment a) {
        return new AppointmentValidation(a).validate(a);
    }

    @Test
    @DisplayName("Throws exception when appointment is null")
    void nullAppointment(){
        IllegalArgumentException ex= assertThrows(IllegalArgumentException.class,
                ()-> runValidation(null));
        assertEquals("Appointment can not be null.",ex.getMessage());
    }

    @Test
    @DisplayName("Throws excpetion when date time is null")
    void nullDateTime(){
        Appointment appointment= new Appointment(1,1,"A",null);
        IllegalArgumentException ex=assertThrows(IllegalArgumentException.class,
                ()->runValidation(appointment));
        assertEquals("Date and time must be filled for appointment.",
                ex.getMessage());
    }

    @Test
    @DisplayName("Throws exception when date time is in past")
    void pastDateTime(){
        Appointment appointment= new Appointment(1,1,"A", LocalDateTime.now().minusHours(1));
        IllegalArgumentException ex=assertThrows(IllegalArgumentException.class,
                ()->runValidation(appointment));
        assertEquals("Date and Time of appointment can not be in past.",
                ex.getMessage());
    }

    @Test
    @DisplayName("Successful Validation")
    void successfullDateTime(){
        Appointment appointment= new Appointment(1,1,"Dummy",LocalDateTime.now().plusMinutes(1));
        assertTrue(runValidation(appointment));
    }
}
