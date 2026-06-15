package com.ethan.employee_system.attendance;

import com.ethan.employee_system.breakrecord.BreakRecordDto;
import com.ethan.employee_system.breakrecord.BreakRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;
    private final BreakRecordService breakRecordService;

    public List<AttendanceDto> findByEmployee(Long employeeId) {
        return repository.findByEmployeeIdOrderByDateDesc(employeeId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<AttendanceDto> findByEmployeeAndMonth(Long employeeId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return repository.findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, start, end).stream()
                .map(this::toDto)
                .toList();
    }

    public AttendanceDto getTodayRecord(Long employeeId) {
        return repository.findByEmployeeIdAndDate(employeeId, LocalDate.now(ZoneOffset.UTC))
                .map(this::toDto)
                .orElse(null);
    }

    @Transactional
    public AttendanceDto checkIn(CheckInRequest request) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        repository.findByEmployeeIdAndDate(request.employeeId(), today).ifPresent(r -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already checked in today");
        });

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeId(request.employeeId());
        record.setDate(today);
        record.setCheckIn(LocalDateTime.now(ZoneOffset.UTC));
        record.setCheckInPhoto(request.checkInPhoto());

        return toDto(repository.save(record));
    }

    @Transactional
    public AttendanceDto checkOut(Long employeeId, CheckOutRequest request) {
        AttendanceRecord record = repository.findByEmployeeIdAndDate(employeeId, LocalDate.now(ZoneOffset.UTC))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No check-in found for today"));

        if (record.getCheckOut() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already checked out today");
        }

        record.setCheckOut(LocalDateTime.now(ZoneOffset.UTC));
        record.setCheckOutPhoto(request.checkOutPhoto());

        return toDto(repository.save(record));
    }

    private AttendanceDto toDto(AttendanceRecord entity) {
        List<BreakRecordDto> breaks = breakRecordService.findByAttendance(entity.getId());
        long totalBreakMs = breakRecordService.getTotalBreakMs(entity.getId());
        return AttendanceDto.from(entity, breaks, totalBreakMs);
    }
}
