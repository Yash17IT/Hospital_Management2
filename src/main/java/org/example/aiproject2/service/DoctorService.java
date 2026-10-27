
package org.example.aiproject2.service;

import org.example.aiproject2.entity.Doctor;
import org.example.aiproject2.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public List<Doctor> getAll() {
        return repository.findAll();
    }

    public Doctor getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
    }

    public Doctor create(Doctor doctor) {
        doctor.setId(null);
        return repository.save(doctor);
    }

    public Doctor update(Long id, Doctor updated) {
        Doctor doctor = getById(id);
        doctor.setName(updated.getName());
        doctor.setSpecialization(updated.getSpecialization());
        doctor.setPhone(updated.getPhone());
        doctor.setEmail(updated.getEmail());
        return repository.save(doctor);
    }

    public void delete(Long id) {
        Doctor doctor = getById(id);
        repository.delete(doctor);
    }
}
