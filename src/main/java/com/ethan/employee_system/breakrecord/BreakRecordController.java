package com.ethan.employee_system.breakrecord;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breaks")
@RequiredArgsConstructor
public class BreakRecordController {

    private final BreakRecordService service;

    @GetMapping("/attendance/{attendanceId}")
    public List<BreakRecordDto> findByAttendance(@PathVariable Long attendanceId) {
        return service.findByAttendance(attendanceId);
    }

    @PostMapping("/employee/{employeeId}/start")
    @ResponseStatus(HttpStatus.CREATED)
    public BreakRecordDto startBreak(@PathVariable Long employeeId, @Valid @RequestBody StartBreakRequest request) {
        return service.startBreak(employeeId, request);
    }

    @PutMapping("/employee/{employeeId}/end")
    public BreakRecordDto endBreak(@PathVariable Long employeeId, @Valid @RequestBody EndBreakRequest request) {
        return service.endBreak(employeeId, request);
    }
}
