-- Password Reset Token table
CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    employer_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_employer FOREIGN KEY (employer_id) REFERENCES employer(employer_id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_token ON password_reset_token(token);
CREATE INDEX idx_password_reset_employer_id ON password_reset_token(employer_id);
CREATE INDEX idx_password_reset_expiry ON password_reset_token(expiry_date);
