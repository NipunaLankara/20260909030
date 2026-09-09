package com.example.traning_management.repo;

import com.example.traning_management.entity.Nomination;

import com.example.traning_management.entity.enums.NominationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {

    boolean existsByOfficerIdAndTrainingProgramId(Long officerId, Long trainingProgramId);

    long countByTrainingProgramIdAndStatus(Long trainingProgramId, NominationStatus status);

    Optional<Nomination> findFirstByTrainingProgramIdAndStatusOrderByNominatedAtAsc(
            Long trainingProgramId,
            NominationStatus status
    );

    boolean existsByOfficerIdAndTrainingProgramIdAndStatusAndNominatedAtAfter(
            Long officerId,
            Long trainingProgramId,
            NominationStatus status,
            LocalDateTime date
    );
}