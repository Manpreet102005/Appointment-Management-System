package repositories;
import entities.Doctor;

import java.util.List;

public interface DoctorRepository {
     boolean addDoctor(Doctor doctor);
     boolean removeDoctor(int id);
     Doctor getDoctor(int id);
     List<Doctor> getAllDoctors();
}
