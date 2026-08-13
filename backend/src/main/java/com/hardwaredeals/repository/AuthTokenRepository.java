package com.hardwaredeals.repository;

import com.hardwaredeals.entity.AuthToken;
import com.hardwaredeals.entity.AuthTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    Optional<AuthToken> findByTokenHashAndType(String tokenHash, AuthTokenType type);

    @Modifying
    @Query("update AuthToken t set t.usedAt = :usedAt where t.user.id = :userId and t.type = :type and t.usedAt is null")
    int revokeActiveTokens(@Param("userId") UUID userId, @Param("type") AuthTokenType type,
                           @Param("usedAt") LocalDateTime usedAt);
}
