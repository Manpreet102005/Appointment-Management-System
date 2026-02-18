package repositries;
import entities.Doctor;
public interface DoctorRepository {
    public void addDoctor(Doctor doctor);
    public void removeDoctor(int id);
}
