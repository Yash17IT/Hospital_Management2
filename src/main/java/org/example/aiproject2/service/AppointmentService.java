
package org.example.aiproject2.service;

import org.example.aiproject2.entity.Appointment;
import org.example.aiproject2.entity.DoctorAvailability;
import org.example.aiproject2.exception.AppointmentConflictException;
import org.example.aiproject2.exception.ResourceNotFoundException;
import org.example.aiproject2.repository.AppointmentRepository;
import org.example.aiproject2.repository.DoctorAvailabilityRepository;
import org.example.aiproject2.repository.DoctorRepository;
import org.example.aiproject2.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentService {

    private static final int DURATION_MINUTES = 30;

    private static final Set<String> VALID_STATUSES = Set.of(
            "SCHEDULED", "COMPLETED", "CANCELLED"
    );

    private static final Set<String> VALID_SORT_FIELDS = Set.of(
            "id", "appointmentDate", "status", "patientId", "doctorId"
    );

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            DoctorAvailabilityRepository availabilityRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // Get all appointments
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));
    }

    // Create appointment
    @Transactional
    public Appointment create(Appointment appointment) {
        validateBooking(appointment, null);

        appointment.setId(null);
        appointment.setStatus("SCHEDULED");

        return appointmentRepository.save(appointment);
    }

    // Update appointment
    @Transactional
    public Appointment update(Long id, Appointment details) {
        Appointment existing = getById(id);

        if ("CANCELLED".equals(existing.getStatus())
                || "COMPLETED".equals(existing.getStatus())) {
            throw new AppointmentConflictException(
                    "Completed or cancelled appointments cannot be rescheduled");
        }

        validateBooking(details, id);

        existing.setPatientId(details.getPatientId());
        existing.setDoctorId(details.getDoctorId());
        existing.setAppointmentDate(details.getAppointmentDate());
        existing.setReason(details.getReason());

        return appointmentRepository.save(existing);
    }

    // Update appointment status
    @Transactional
    public Appointment updateStatus(Long id, String status) {
        Appointment appointment = getById(id);

        if (status == null
                || !VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status must be SCHEDULED, COMPLETED, or CANCELLED");
        }

        appointment.setStatus(status.toUpperCase());
        return appointmentRepository.save(appointment);
    }

    // Delete appointment
    public void delete(Long id) {
        appointmentRepository.delete(getById(id));
    }

    // Search by patient
    public List<Appointment> getByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId);
        }

        return appointmentRepository.findByPatientId(patientId);
    }

    // Search by doctor
    public List<Appointment> getByDoctorId(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + doctorId);
        }

        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Search by status
    public List<Appointment> getByStatus(String status) {
        validateStatus(status);
        return appointmentRepository.findByStatusIgnoreCase(
                status.toUpperCase());
    }

    // Search by date
    public List<Appointment> getByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return appointmentRepository
                .findByAppointmentDateGreaterThanEqualAndAppointmentDateLessThan(
                        start, end);
    }

    // Paginated appointments with sorting
    public Page<Appointment> getAppointmentsPage(
            int page, int size, String sortBy, String direction) {

        Pageable pageable = createPageable(page, size, sortBy, direction);
        return appointmentRepository.findAll(pageable);
    }

    // Paginated appointments filtered by status
    public Page<Appointment> getAppointmentsByStatusPage(
            String status, int page, int size,
            String sortBy, String direction) {

        validateStatus(status);

        Pageable pageable = createPageable(page, size, sortBy, direction);

        return appointmentRepository.findByStatusIgnoreCase(
                status.toUpperCase(), pageable);
    }

    // Create validated pagination and sorting settings
    private Pageable createPageable(
            int page, int size, String sortBy, String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        if (sortBy == null || !VALID_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Use id, appointmentDate, status, patientId, or doctorId");
        }

        if (direction == null) {
            throw new IllegalArgumentException(
                    "Direction must be ASC or DESC");
        }

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Direction must be ASC or DESC");
        }

        return PageRequest.of(
                page, size, Sort.by(sortDirection, sortBy));
    }

    // Validate booking details
    private void validateBooking(
            Appointment appointment, Long ignoredAppointmentId) {

        if (appointment.getPatientId() == null
                || !patientRepository.existsById(appointment.getPatientId())) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + appointment.getPatientId());
        }

        if (appointment.getDoctorId() == null
                || !doctorRepository.existsById(appointment.getDoctorId())) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + appointment.getDoctorId());
        }

        LocalDateTime start = appointment.getAppointmentDate();

        if (start == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required");
        }

        if (!start.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Appointment must be scheduled in the future");
        }

        LocalDateTime end = start.plusMinutes(DURATION_MINUTES);

        validateDoctorAvailability(appointment.getDoctorId(), start, end);

        List<Appointment> nearbyAppointments =
                appointmentRepository
                        .findByDoctorIdAndStatusAndAppointmentDateGreaterThanAndAppointmentDateLessThan(
                                appointment.getDoctorId(),
                                "SCHEDULED",
                                start.minusMinutes(DURATION_MINUTES),
                                start.plusMinutes(DURATION_MINUTES));

        for (Appointment other : nearbyAppointments) {
            if (ignoredAppointmentId != null
                    && ignoredAppointmentId.equals(other.getId())) {
                continue;
            }

            LocalDateTime otherStart = other.getAppointmentDate();
            LocalDateTime otherEnd =
                    otherStart.plusMinutes(DURATION_MINUTES);

            boolean overlaps = start.isBefore(otherEnd)
                    && end.isAfter(otherStart);

            if (overlaps) {
                throw new AppointmentConflictException(
                        "Doctor already has an appointment during this time");
            }
        }
    }

    // Check doctor's working hours
    private void validateDoctorAvailability(
            Long doctorId, LocalDateTime start, LocalDateTime end) {

        List<DoctorAvailability> slots =
                availabilityRepository.findByDoctorIdAndDayOfWeek(
                        doctorId, start.getDayOfWeek());

        LocalTime requestedStart = start.toLocalTime();
        LocalTime requestedEnd = end.toLocalTime();

        boolean available = slots.stream().anyMatch(slot ->
                !requestedStart.isBefore(slot.getStartTime())
                        && !requestedEnd.isAfter(slot.getEndTime()));

        if (!available) {
            throw new AppointmentConflictException(
                    "Doctor is not available at the requested date and time");
        }
    }

    private void validateStatus(String status) {
        if (status == null
                || !VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status must be SCHEDULED, COMPLETED, or CANCELLED");
        }
    }
}
