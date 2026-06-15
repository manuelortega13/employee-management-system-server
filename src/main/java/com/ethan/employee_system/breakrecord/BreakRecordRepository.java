package com.ethan.employee_system.breakrecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BreakRecordRepository extends JpaRepository<BreakRecord, Long> {

    List<BreakRecord> findByAttendanceIdOrderByStartTimeAsc(Long attendanceId);

    Optional<BreakRecord> findByAttendanceIdAndEndTimeIsNull(Long attendanceId);
}
