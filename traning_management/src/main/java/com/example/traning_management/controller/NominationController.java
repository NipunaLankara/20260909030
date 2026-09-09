package com.example.traning_management.controller;

import com.example.traning_management.dto.request.NominationRequestDTO;
import com.example.traning_management.dto.response.NominationResponseDTO;
import com.example.traning_management.service.NominationService;
import com.example.traning_management.utill.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nominations")
public class NominationController {

    @Autowired
    private NominationService nominationService;

    @PostMapping("/add")
    public ResponseEntity<StandardResponse> createNomination(
            @RequestBody NominationRequestDTO request
    ) {
        NominationResponseDTO response = nominationService.createNomination(request);

        StandardResponse standardResponse = new StandardResponse(
                HttpStatus.CREATED.value(),
                "Nomination processed successfully",
                response
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(standardResponse);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<StandardResponse> cancelNomination(
            @PathVariable Long id
    ) {
        NominationResponseDTO response = nominationService.cancelNomination(id);

        StandardResponse standardResponse = new StandardResponse(
                HttpStatus.OK.value(),
                "Nomination cancelled successfully",
                response
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(standardResponse);
    }
}