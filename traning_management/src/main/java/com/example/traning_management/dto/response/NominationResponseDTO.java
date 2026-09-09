package com.example.traning_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NominationResponseDTO {

    private Long id;
    private Long officerId;
    private String officerName;
    private Long departmentId;
    private String departmentName;
    private Long trainingProgramId;
    private String trainingProgramTitle;
    private LocalDateTime nominatedAt;
    private String status;
}