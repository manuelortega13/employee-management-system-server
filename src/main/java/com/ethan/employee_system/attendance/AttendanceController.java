package com.ethan.employee_system.attendance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @GetMapping("/employee/{employeeId}")
    public List<AttendanceDto> findByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        if (year != null && month != null) {
            return service.findByEmployeeAndMonth(employeeId, year, month);
        }
        return service.findByEmployee(employeeId);
    }

    @GetMapping("/employee/{employeeId}/today")
    public AttendanceDto getTodayRecord(@PathVariable Long employeeId) {
        return service.getTodayRecord(employeeId);
    }

    @PostMapping("/check-in")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceDto checkIn(@Valid @RequestBody CheckInRequest request) {
        return service.checkIn(request);
    }

    @PutMapping("/employee/{employeeId}/check-out")
    public AttendanceDto checkOut(@PathVariable Long employeeId, @Valid @RequestBody CheckOutRequest request) {
        return service.checkOut(employeeId, request);
    }
}
