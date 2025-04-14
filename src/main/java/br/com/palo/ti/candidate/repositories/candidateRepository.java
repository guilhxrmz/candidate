package br.com.palo.ti.candidate.repositories;

import br.com.palo.ti.candidate.models.candidateModel;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface candidateRepository extends JpaRepository<candidateModel, UUID> {
    Optional<candidateModel> findByEmail(@Email String email);
}