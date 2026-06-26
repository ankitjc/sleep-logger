-- USERS TABLE
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- SLEEP LOGS TABLE
CREATE TABLE sleep_logs (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,

    sleep_date DATE NOT NULL,
    bed_time TIMESTAMP NOT NULL,
    wake_time TIMESTAMP NOT NULL,

    total_time_in_bed_minutes INTEGER NOT NULL,

    feeling VARCHAR(10) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_sleep_logs_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- PERFORMANCE INDEXES
CREATE INDEX idx_sleep_logs_user_date
    ON sleep_logs(user_id, sleep_date DESC);

CREATE INDEX idx_sleep_logs_user_created
    ON sleep_logs(user_id, created_at DESC);