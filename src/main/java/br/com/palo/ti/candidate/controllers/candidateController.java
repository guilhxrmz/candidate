package br.com.palo.ti.candidate.controllers;

import br.com.palo.ti.candidate.dtos.candidateDto;
import br.com.palo.ti.candidate.services.candidateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/candidates")
public class candidateController {

    @Autowired
    private candidateService service;

    @Operation(summary = "Retrieve all candidates", description = "Fetches a list of all candidates.")
    @GetMapping
    public ResponseEntity<List<candidateDto>> getAllCandidates() {
        return ResponseEntity.ok(service.getAllCandidates());
    }

    @Operation(summary = "Retrieve a candidate by ID", description = "Fetches a candidate by their unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<candidateDto> getCandidateById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCandidateById(id));
    }

    @Operation(summary = "Create a new candidate", description = "Creates a new candidate with the provided details.")
    @PostMapping
    public ResponseEntity<candidateDto> createCandidate(@RequestBody @Valid candidateDto candidateDto) {
        return ResponseEntity.ok(service.createCandidate(candidateDto));
    }

    @Operation(summary = "Update a candidate", description = "Updates the details of an existing candidate.")
    @PatchMapping("/{id}")
    public ResponseEntity<candidateDto> updateCandidate(@PathVariable UUID id, @RequestBody candidateDto candidateDto) {
        return ResponseEntity.ok(service.updateCandidate(id, candidateDto));
    }

    @Operation(summary = "Delete a candidate", description = "Deletes a candidate by their unique identifier.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable UUID id) {
        service.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}