package repositries;
import entities.Doctor;

import java.util.List;

public interface DoctorRepository {
     void addDoctor(Doctor doctor);
     void removeDoctor(int id);
     Doctor getDoctor(int id);
     List<Doctor> getAllDoctors();
}
