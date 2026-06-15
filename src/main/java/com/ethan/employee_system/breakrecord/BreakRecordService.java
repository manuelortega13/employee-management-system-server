package com.ethan.employee_system.breakrecord;

import com.ethan.employee_system.attendance.AttendanceRecord;
import com.ethan.employee_system.attendance.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreakRecordService {

    private final BreakRecordRepository breakRepository;
    private final AttendanceRepository attendanceRepository;

    public List<BreakRecordDto> findByAttendance(Long attendanceId) {
        return breakRepository.findByAttendanceIdOrderByStartTimeAsc(attendanceId).stream()
                .map(BreakRecordDto::from)
                .toList();
    }

    @Transactional
    public BreakRecordDto startBreak(Long employeeId, StartBreakRequest request) {
        AttendanceRecord attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, LocalDate.now(ZoneOffset.UTC))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No check-in found for today"));

        if (attendance.getCheckOut() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already checked out for today");
        }

        breakRepository.findByAttendanceIdAndEndTimeIsNull(attendance.getId()).ifPresent(b -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already on a break");
        });

        BreakRecord record = new BreakRecord();
        record.setAttendanceId(attendance.getId());
        record.setStartTime(LocalDateTime.now(ZoneOffset.UTC));
        record.setStartPhoto(request.startPhoto());

        return BreakRecordDto.from(breakRepository.save(record));
    }

    @Transactional
    public BreakRecordDto endBreak(Long employeeId, EndBreakRequest request) {
        AttendanceRecord attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, LocalDate.now(ZoneOffset.UTC))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No check-in found for today"));

        BreakRecord activeBreak = breakRepository.findByAttendanceIdAndEndTimeIsNull(attendance.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active break found"));

        activeBreak.setEndTime(LocalDateTime.now(ZoneOffset.UTC));
        activeBreak.setEndPhoto(request.endPhoto());

        return BreakRecordDto.from(breakRepository.save(activeBreak));
    }

    public long getTotalBreakMs(Long attendanceId) {
        return breakRepository.findByAttendanceIdOrderByStartTimeAsc(attendanceId).stream()
                .filter(b -> b.getEndTime() != null)
                .mapToLong(b -> Duration.between(b.getStartTime(), b.getEndTime()).toMillis())
                .sum();
    }
}
