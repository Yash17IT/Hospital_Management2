
package org.example.aiproject2.service;

import org.example.aiproject2.repository.AppointmentRepository;
import org.example.aiproject2.repository.DoctorRepository;
import org.example.aiproject2.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DashboardService(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository) {

        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Map<String, Object> getDashboardStatistics() {

        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        long totalAppointments = appointmentRepository.count();

        long scheduledAppointments =
                appointmentRepository.countByStatusIgnoreCase("SCHEDULED");

        long completedAppointments =
                appointmentRepository.countByStatusIgnoreCase("COMPLETED");

        long cancelledAppointments =
                appointmentRepository.countByStatusIgnoreCase("CANCELLED");

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long todaysAppointments =
                appointmentRepository
                        .countByAppointmentDateGreaterThanEqualAndAppointmentDateLessThan(
                                start, end);

        Map<String, Object> statistics = new LinkedHashMap<>();

        statistics.put("totalPatients", totalPatients);
        statistics.put("totalDoctors", totalDoctors);
        statistics.put("totalAppointments", totalAppointments);
        statistics.put("scheduledAppointments", scheduledAppointments);
        statistics.put("completedAppointments", completedAppointments);
        statistics.put("cancelledAppointments", cancelledAppointments);
        statistics.put("todaysAppointments", todaysAppointments);

        return statistics;
    }
}
