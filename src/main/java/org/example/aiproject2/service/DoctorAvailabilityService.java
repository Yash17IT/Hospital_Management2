
package org.example.aiproject2.service;

import org.example.aiproject2.entity.Doctor;
import org.example.aiproject2.entity.DoctorAvailability;
import org.example.aiproject2.exception.AppointmentConflictException;
import org.example.aiproject2.exception.ResourceNotFoundException;
import org.example.aiproject2.repository.DoctorAvailabilityRepository;
import org.example.aiproject2.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    public DoctorAvailabilityService(
            DoctorAvailabilityRepository availabilityRepository,
            DoctorRepository doctorRepository) {
        this.availabilityRepository = availabilityRepository;
        this.doctorRepository = doctorRepository;
    }

    public DoctorAvailability addAvailability(
            Long doctorId, DoctorAvailability availability) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));

        if (availability.getDayOfWeek() == null
                || availability.getStartTime() == null
                || availability.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "Day, start time and end time are required");
        }

        if (!availability.getStartTime()
                .isBefore(availability.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        List<DoctorAvailability> existing =
                availabilityRepository.findByDoctorIdAndDayOfWeek(
                        doctorId, availability.getDayOfWeek());

        for (DoctorAvailability slot : existing) {
            boolean overlaps =
                    availability.getStartTime().isBefore(slot.getEndTime())
                            && availability.getEndTime().isAfter(slot.getStartTime());

            if (overlaps) {
                throw new AppointmentConflictException(
                        "This availability overlaps an existing time period");
            }
        }

        availability.setId(null);
        availability.setDoctorId(doctorId);

        return availabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getAvailability(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + doctorId);
        }

        return availabilityRepository.findByDoctorId(doctorId);
    }

    public void deleteAvailability(Long availabilityId) {
        DoctorAvailability availability =
                availabilityRepository.findById(availabilityId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Availability not found with id: "
                                        + availabilityId));

        availabilityRepository.delete(availability);
    }
}
