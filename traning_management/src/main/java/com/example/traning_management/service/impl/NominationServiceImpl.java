package com.example.traning_management.service.impl;

import com.example.traning_management.dto.request.NominationRequestDTO;
import com.example.traning_management.dto.response.NominationResponseDTO;
import com.example.traning_management.entity.Department;
import com.example.traning_management.entity.Nomination;
import com.example.traning_management.entity.Officer;
import com.example.traning_management.entity.TrainingProgram;
import com.example.traning_management.exception.DuplicateNominationException;
import com.example.traning_management.repo.DepartmentRepository;
import com.example.traning_management.repo.NominationRepository;
import com.example.traning_management.repo.OfficerRepository;
import com.example.traning_management.repo.TrainingProgramRepository;
import com.example.traning_management.service.NominationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public NominationResponseDTO createNomination(
            NominationRequestDTO request
    ) {

        // 1. Check duplicate
        boolean alreadyNominated =
                nominationRepository
                        .existsByOfficerIdAndTrainingProgramId(
                                request.getOfficerId(),
                                request.getTrainingProgramId()
                        );

        if (alreadyNominated) {
            throw new DuplicateNominationException(
                    "Officer is already nominated for this training programme"
            );
        }

        // 2. Get officer
        Officer officer = officerRepository
                .findById(request.getOfficerId())
                .orElseThrow(() ->
                        new RuntimeException("Officer not found")
                );

        // 3. Get department
        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new RuntimeException("Department not found")
                );

        // 4. Get training programme
        TrainingProgram trainingProgram =
                trainingProgramRepository
                        .findById(request.getTrainingProgramId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Training programme not found"
                                )
                        );

        // 5. Create nomination
        Nomination nomination = new Nomination();

        nomination.setOfficer(officer);
        nomination.setDepartment(department);
        nomination.setTrainingProgram(trainingProgram);
        nomination.setNominatedAt(LocalDateTime.now());

        // 6. Save
        Nomination saved =
                nominationRepository.save(nomination);

        // 7. Convert to DTO
        return convertToDTO(saved);
    }

    private NominationResponseDTO convertToDTO(
            Nomination nomination
    ) {

        NominationResponseDTO response =
                new NominationResponseDTO();

        response.setId(nomination.getId());

        response.setOfficerId(nomination.getOfficer().getId());

        response.setOfficerName(nomination.getOfficer().getFirstName() + " " + nomination.getOfficer().getLastName()
        );

        response.setDepartmentId(
                nomination.getDepartment().getId()
        );

        response.setDepartmentName(
                nomination.getDepartment().getName()
        );

        response.setTrainingProgramId(
                nomination.getTrainingProgram().getId()
        );

        response.setTrainingProgramTitle(
                nomination.getTrainingProgram().getTitle()
        );

        response.setNominatedAt(
                nomination.getNominatedAt()
        );

        return response;
    }

}