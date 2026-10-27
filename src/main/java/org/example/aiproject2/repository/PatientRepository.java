
package org.example.aiproject2.repository;

import org.example.aiproject2.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
