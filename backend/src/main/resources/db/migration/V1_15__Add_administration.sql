ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';
ALTER TABLE users ADD CONSTRAINT chk_users_role CHECK (role IN ('USER','ADMIN'));

CREATE TABLE admin_audit (
    id UUID PRIMARY KEY,
    admin_id UUID,
    action VARCHAR(80) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_audit_admin FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_admin_audit_admin_id ON admin_audit(admin_id);
CREATE INDEX idx_admin_audit_created_at ON admin_audit(created_at);
