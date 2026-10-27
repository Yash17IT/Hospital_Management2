
package org.example.aiproject2.controller;

import jakarta.validation.Valid;
import org.example.aiproject2.entity.Appointment;
import org.example.aiproject2.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Get all appointments
    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    // Create appointment
    @PostMapping
    public Appointment create(
            @Valid @RequestBody Appointment appointment) {
        return appointmentService.create(appointment);
    }

    // Update appointment
    @PutMapping("/{id}")
    public Appointment update(
            @PathVariable Long id,
            @Valid @RequestBody Appointment appointment) {
        return appointmentService.update(id, appointment);
    }

    // Update appointment status
    @PatchMapping("/{id}/status")
    public Appointment updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return appointmentService.updateStatus(id, status);
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return "Appointment deleted successfully";
    }

    // Search by patient ID
    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatientId(
            @PathVariable Long patientId) {
        return appointmentService.getByPatientId(patientId);
    }

    // Search by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getByDoctorId(
            @PathVariable Long doctorId) {
        return appointmentService.getByDoctorId(doctorId);
    }

    // Search by status
    @GetMapping("/status")
    public List<Appointment> getByStatus(
            @RequestParam String status) {
        return appointmentService.getByStatus(status);
    }

    // Search by date
    @GetMapping("/date")
    public List<Appointment> getByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return appointmentService.getByDate(date);
    }

    // Paginated appointments with sorting
    @GetMapping("/search/page")
    public Page<Appointment> getAppointmentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return appointmentService.getAppointmentsPage(
                page, size, sortBy, direction);
    }

    // Paginated appointments filtered by status
    @GetMapping("/search/page/status")
    public Page<Appointment> getAppointmentsByStatusPage(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        return appointmentService.getAppointmentsByStatusPage(
                status, page, size, sortBy, direction);
    }
}
