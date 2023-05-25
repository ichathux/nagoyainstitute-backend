package com.nagoyainstitute.backendnagoyainstitute.repository;

import com.nagoyainstitute.backendnagoyainstitute.model.VerifcationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerifcationToken, Long> {
    Optional<VerifcationToken> findByToken(String token);
}
