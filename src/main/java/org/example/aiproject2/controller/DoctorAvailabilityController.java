
package org.example.aiproject2.controller;

import jakarta.validation.Valid;
import org.example.aiproject2.entity.DoctorAvailability;
import org.example.aiproject2.service.DoctorAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}/availability")
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    public DoctorAvailabilityController(
            DoctorAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public DoctorAvailability addAvailability(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorAvailability availability) {

        return availabilityService.addAvailability(
                doctorId, availability);
    }

    @GetMapping
    public List<DoctorAvailability> getAvailability(
            @PathVariable Long doctorId) {

        return availabilityService.getAvailability(doctorId);
    }

    @DeleteMapping("/{availabilityId}")
    public String deleteAvailability(
            @PathVariable Long doctorId,
            @PathVariable Long availabilityId) {

        availabilityService.getAvailability(doctorId);

        availabilityService.deleteAvailability(availabilityId);

        return "Doctor availability deleted successfully";
    }
}
