package com.example.traning_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NominationRequestDTO {

    private Long officerId;
    private Long departmentId;
    private Long trainingProgramId;

}