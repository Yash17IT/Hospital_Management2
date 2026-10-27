
package org.example.aiproject2.service;

import org.example.aiproject2.entity.Patient;
import org.example.aiproject2.exception.ResourceNotFoundException;
import org.example.aiproject2.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> getAll() {
        return repository.findAll();
    }

    public Patient getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found: " + id));
    }

    public Patient create(Patient patient) {
        patient.setId(null);
        return repository.save(patient);
    }

    public Patient update(Long id, Patient updated) {
        Patient patient = getById(id);
        patient.setName(updated.getName());
        patient.setAge(updated.getAge());
        patient.setGender(updated.getGender());
        patient.setPhone(updated.getPhone());
        patient.setEmail(updated.getEmail());
        return repository.save(patient);
    }

    public void delete(Long id) {
        Patient patient = getById(id);
        repository.delete(patient);
    }
}
