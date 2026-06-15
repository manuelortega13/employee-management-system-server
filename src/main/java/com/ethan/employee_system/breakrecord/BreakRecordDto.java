package com.ethan.employee_system.breakrecord;

import java.time.LocalDateTime;

public record BreakRecordDto(
        Long id,
        Long attendanceId,
        LocalDateTime startTime,
        String startPhoto,
        LocalDateTime endTime,
        String endPhoto
) {
    public static BreakRecordDto from(BreakRecord entity) {
        return new BreakRecordDto(
                entity.getId(),
                entity.getAttendanceId(),
                entity.getStartTime(),
                entity.getStartPhoto(),
                entity.getEndTime(),
                entity.getEndPhoto()
        );
    }
}
