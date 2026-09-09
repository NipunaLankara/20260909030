package com.example.traning_management.service;

import com.example.traning_management.dto.request.NominationRequestDTO;
import com.example.traning_management.dto.response.NominationResponseDTO;

public interface NominationService {

    NominationResponseDTO createNomination(NominationRequestDTO request);

    NominationResponseDTO cancelNomination(Long nominationId);
}