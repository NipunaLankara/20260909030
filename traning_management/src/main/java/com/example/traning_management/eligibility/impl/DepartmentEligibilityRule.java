package com.example.traning_management.eligibility.impl;

import com.example.traning_management.eligibility.EligibilityRule;
import com.example.traning_management.entity.Officer;
import com.example.traning_management.entity.TrainingProgram;
import com.example.traning_management.exception.IneligibleOfficerException;
import org.springframework.stereotype.Component;

@Component
public class DepartmentEligibilityRule implements EligibilityRule {

    @Override
    public void validate(Officer officer, TrainingProgram trainingProgram) {
        if (trainingProgram.getAllowedDepartments() != null && !trainingProgram.getAllowedDepartments().isEmpty()) {
            boolean isAllowed = trainingProgram.getAllowedDepartments().stream()
                    .anyMatch(dept -> dept.getId().equals(officer.getDepartment().getId()));

            if (!isAllowed) {
                throw new IneligibleOfficerException(
                        "Officer's department (" + officer.getDepartment().getName() +
                                ") is not eligible for this training programme"
                );
            }
        }
    }
}