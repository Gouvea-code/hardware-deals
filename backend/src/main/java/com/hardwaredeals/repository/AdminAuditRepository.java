package com.hardwaredeals.repository;

import com.hardwaredeals.entity.AdminAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AdminAuditRepository extends JpaRepository<AdminAudit,UUID>{
 List<AdminAudit> findTop100ByOrderByCreatedAtDesc();
 void deleteByAdminId(UUID adminId);
}
