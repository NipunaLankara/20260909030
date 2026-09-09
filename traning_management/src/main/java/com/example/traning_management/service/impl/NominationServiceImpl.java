package com.example.traning_management.service.impl;

import com.example.traning_management.dto.request.NominationRequestDTO;
import com.example.traning_management.dto.response.NominationResponseDTO;
import com.example.traning_management.eligibility.EligibilityRule;
import com.example.traning_management.entity.Department;
import com.example.traning_management.entity.Nomination;
import com.example.traning_management.entity.Officer;
import com.example.traning_management.entity.TrainingProgram;
import com.example.traning_management.entity.enums.NominationStatus;
import com.example.traning_management.exception.DuplicateNominationException;
import com.example.traning_management.repo.DepartmentRepository;
import com.example.traning_management.repo.NominationRepository;
import com.example.traning_management.repo.OfficerRepository;
import com.example.traning_management.repo.TrainingProgramRepository;
import com.example.traning_management.service.NominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NominationServiceImpl implements NominationService {

    @Autowired
    private NominationRepository nominationRepository;
    @Autowired
    private OfficerRepository officerRepository;
    @Autowired
    private TrainingProgramRepository trainingProgramRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private List<EligibilityRule> eligibilityRules;

    @Override
    @Transactional
    public NominationResponseDTO createNomination(NominationRequestDTO request) {

        // 1. Check duplicate nomination
        boolean alreadyNominated = nominationRepository
                .existsByOfficerIdAndTrainingProgramId(
                        request.getOfficerId(),
                        request.getTrainingProgramId()
                );

        if (alreadyNominated) {
            throw new DuplicateNominationException(
                    "Officer is already nominated for this training programme"
            );
        }


        Officer officer = officerRepository.findById(request.getOfficerId())
                .orElseThrow(() -> new RuntimeException("Officer not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        TrainingProgram trainingProgram = trainingProgramRepository.findById(request.getTrainingProgramId())
                .orElseThrow(() -> new RuntimeException("Training programme not found"));

        //  Validate all eligibility rules dynamically
        for (EligibilityRule rule : eligibilityRules) {
            rule.validate(officer, trainingProgram);
        }

        // 5. Check capacity and assign status
        long confirmedCount = nominationRepository.countByTrainingProgramIdAndStatus(
                trainingProgram.getId(),
                NominationStatus.CONFIRMED
        );

        NominationStatus initialStatus;
        if (confirmedCount < trainingProgram.getMaxParticipants()) {
            initialStatus = NominationStatus.CONFIRMED;
        } else {
            initialStatus = NominationStatus.WAITING_LIST;
        }

        // 6. Create nomination
        Nomination nomination = new Nomination();
        nomination.setOfficer(officer);
        nomination.setDepartment(department);
        nomination.setTrainingProgram(trainingProgram);
        nomination.setNominatedAt(LocalDateTime.now());
        nomination.setStatus(initialStatus);

        // 7. Save nomination
        Nomination saved = nominationRepository.save(nomination);

        // 8. Convert to DTO
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public NominationResponseDTO cancelNomination(Long nominationId) {

        // 1. Fetch existing nomination
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new RuntimeException("Nomination not found"));

        NominationStatus currentStatus = nomination.getStatus();

        if (currentStatus == NominationStatus.CANCELLED) {
            throw new RuntimeException("Nomination is already cancelled");
        }

        // 2. Mark nomination as CANCELLED
        nomination.setStatus(NominationStatus.CANCELLED);
        Nomination updated = nominationRepository.save(nomination);

        // 3. If a CONFIRMED seat was freed up, promote the earliest WAITING_LIST participant
        if (currentStatus == NominationStatus.CONFIRMED) {
            nominationRepository.findFirstByTrainingProgramIdAndStatusOrderByNominatedAtAsc(
                    nomination.getTrainingProgram().getId(),
                    NominationStatus.WAITING_LIST
            ).ifPresent(nextParticipant -> {
                nextParticipant.setStatus(NominationStatus.CONFIRMED);
                nominationRepository.save(nextParticipant);
            });
        }

        return convertToDTO(updated);
    }

    private NominationResponseDTO convertToDTO(Nomination nomination) {
        NominationResponseDTO response = new NominationResponseDTO();

        response.setId(nomination.getId());
        response.setOfficerId(nomination.getOfficer().getId());
        response.setOfficerName(
                nomination.getOfficer().getFirstName() + " " + nomination.getOfficer().getLastName()
        );
        response.setDepartmentId(nomination.getDepartment().getId());
        response.setDepartmentName(nomination.getDepartment().getName());
        response.setTrainingProgramId(nomination.getTrainingProgram().getId());
        response.setTrainingProgramTitle(nomination.getTrainingProgram().getTitle());
        response.setNominatedAt(nomination.getNominatedAt());
        response.setStatus(nomination.getStatus().name());

        return response;
    }
}