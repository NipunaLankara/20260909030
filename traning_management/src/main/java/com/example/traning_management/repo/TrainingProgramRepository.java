package com.example.traning_management.repo;

import com.example.traning_management.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgramRepository
        extends JpaRepository<TrainingProgram, Long> {
}