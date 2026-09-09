package com.example.traning_management.eligibility;

import com.example.traning_management.entity.Officer;
import com.example.traning_management.entity.TrainingProgram;

public interface EligibilityRule {
    void validate(Officer officer, TrainingProgram trainingProgram);
}