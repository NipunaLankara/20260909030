package com.example.traning_management.repo;

import com.example.traning_management.entity.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NominationRepository
        extends JpaRepository<Nomination, Long> {

    boolean existsByOfficerIdAndTrainingProgramId(
            Long officerId,
            Long trainingProgramId
    );
}