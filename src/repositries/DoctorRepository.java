package repositries;
import entities.Doctor;

import java.util.List;

public interface DoctorRepository {
    public void addDoctor(Doctor doctor);
    public void removeDoctor(int id);
    public Doctor getDoctor(int id);
    public List<Doctor> getAllDoctors();
}
