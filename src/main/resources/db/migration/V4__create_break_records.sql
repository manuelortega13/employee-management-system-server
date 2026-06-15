CREATE TABLE break_records (
    id              BIGSERIAL PRIMARY KEY,
    attendance_id   BIGINT NOT NULL REFERENCES attendance_records(id) ON DELETE CASCADE,
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_break_records_attendance ON break_records(attendance_id);
