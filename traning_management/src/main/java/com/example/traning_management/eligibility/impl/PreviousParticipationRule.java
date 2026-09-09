package com.example.traning_management.eligibility.impl;

import com.example.traning_management.eligibility.EligibilityRule;
import com.example.traning_management.entity.Officer;
import com.example.traning_management.entity.TrainingProgram;
import com.example.traning_management.entity.enums.NominationStatus;
import com.example.traning_management.exception.IneligibleOfficerException;
import com.example.traning_management.repo.NominationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PreviousParticipationRule implements EligibilityRule {

    @Autowired
    private NominationRepository nominationRepository;

    @Override
    public void validate(Officer officer, TrainingProgram trainingProgram) {
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);

        boolean recentParticipation = nominationRepository
                .existsByOfficerIdAndTrainingProgramIdAndStatusAndNominatedAtAfter(
                        officer.getId(),
                        trainingProgram.getId(),
                        NominationStatus.CONFIRMED,
                        twelveMonthsAgo
                );

        if (recentParticipation) {
            throw new IneligibleOfficerException(
                    "Officer has already completed/participated in this training programme within the last 12 months"
            );
        }
    }
}