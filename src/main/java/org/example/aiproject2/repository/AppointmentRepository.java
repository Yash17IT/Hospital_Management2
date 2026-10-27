
package org.example.aiproject2.repository;

import org.example.aiproject2.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    // Search appointments by patient ID
    List<Appointment> findByPatientId(Long patientId);

    // Search appointments by doctor ID
    List<Appointment> findByDoctorId(Long doctorId);

    // Search appointments by status
    List<Appointment> findByStatusIgnoreCase(String status);

    // Search appointments by status with pagination
    Page<Appointment> findByStatusIgnoreCase(
            String status,
            Pageable pageable
    );

    // Search appointments by date range
    List<Appointment>
    findByAppointmentDateGreaterThanEqualAndAppointmentDateLessThan(
            LocalDateTime start,
            LocalDateTime end
    );

    // Find nearby scheduled appointments for conflict checking
    List<Appointment>
    findByDoctorIdAndStatusAndAppointmentDateGreaterThanAndAppointmentDateLessThan(
            Long doctorId,
            String status,
            LocalDateTime start,
            LocalDateTime end
    );
    long countByStatusIgnoreCase(String status);

    long countByAppointmentDateGreaterThanEqualAndAppointmentDateLessThan(
            java.time.LocalDateTime start,
            java.time.LocalDateTime end
    );
}
