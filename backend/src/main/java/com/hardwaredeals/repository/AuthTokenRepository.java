package com.hardwaredeals.repository;

import com.hardwaredeals.entity.AuthToken;
import com.hardwaredeals.entity.AuthTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    Optional<AuthToken> findByTokenHashAndType(String tokenHash, AuthTokenType type);
}
