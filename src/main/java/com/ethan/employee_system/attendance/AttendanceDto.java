package com.ethan.employee_system.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceDto(
        Long id,
        Long employeeId,
        LocalDate date,
        LocalDateTime checkIn,
        String checkInPhoto,
        LocalDateTime checkOut,
        String checkOutPhoto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AttendanceDto from(AttendanceRecord entity) {
        return new AttendanceDto(
                entity.getId(),
                entity.getEmployeeId(),
                entity.getDate(),
                entity.getCheckIn(),
                entity.getCheckInPhoto(),
                entity.getCheckOut(),
                entity.getCheckOutPhoto(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
