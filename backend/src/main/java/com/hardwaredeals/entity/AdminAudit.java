package com.hardwaredeals.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="admin_audit",indexes={@Index(name="idx_admin_audit_admin_id",columnList="admin_id"),
        @Index(name="idx_admin_audit_created_at",columnList="created_at")})
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminAudit {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="admin_id") private User admin;
 @Column(nullable=false,length=80) private String action;
 @Column(name="target_type",nullable=false,length=50) private String targetType;
 @Column(name="target_id",nullable=false) private UUID targetId;
 @Column(name="created_at",nullable=false,updatable=false) @Builder.Default private LocalDateTime createdAt=LocalDateTime.now();
}
