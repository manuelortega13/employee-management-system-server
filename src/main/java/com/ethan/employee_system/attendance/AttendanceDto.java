package com.ethan.employee_system.attendance;

import com.ethan.employee_system.breakrecord.BreakRecordDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AttendanceDto(
        Long id,
        Long employeeId,
        LocalDate date,
        LocalDateTime checkIn,
        String checkInPhoto,
        LocalDateTime checkOut,
        String checkOutPhoto,
        List<BreakRecordDto> breaks,
        Long totalBreakMs,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AttendanceDto from(AttendanceRecord entity, List<BreakRecordDto> breaks, long totalBreakMs) {
        return new AttendanceDto(
                entity.getId(),
                entity.getEmployeeId(),
                entity.getDate(),
                entity.getCheckIn(),
                entity.getCheckInPhoto(),
                entity.getCheckOut(),
                entity.getCheckOutPhoto(),
                breaks,
                totalBreakMs,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
