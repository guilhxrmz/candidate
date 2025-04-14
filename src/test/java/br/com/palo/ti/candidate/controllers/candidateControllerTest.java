package br.com.palo.ti.candidate.controllers;

import br.com.palo.ti.candidate.dtos.candidateDto;
import br.com.palo.ti.candidate.services.candidateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class candidateControllerTest {

    @Mock
    private candidateService service;

    @InjectMocks
    private candidateController controller;

    @Test
    void getAllCandidatesReturnsListOfCandidates() {
        List<candidateDto> candidates = List.of(
                new candidateDto(UUID.randomUUID(), "John Doe", "john.doe@example.com", "1234567890"),
                new candidateDto(UUID.randomUUID(), "Jane Doe", "jane.doe@example.com", "0987654321")
        );
        when(service.getAllCandidates()).thenReturn(candidates);

        ResponseEntity<List<candidateDto>> response = controller.getAllCandidates();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidates, response.getBody());
    }

    @Test
    void getCandidateByIdReturnsCandidateWhenFound() {
        UUID id = UUID.randomUUID();
        candidateDto candidate = new candidateDto(id, "John Doe", "john.doe@example.com", "1234567890");
        when(service.getCandidateById(id)).thenReturn(candidate);

        ResponseEntity<candidateDto> response = controller.getCandidateById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
    }

    @Test
    void getCandidateByIdThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(service.getCandidateById(id)).thenThrow(new RuntimeException("Candidate not found"));

        assertThrows(RuntimeException.class, () -> controller.getCandidateById(id));
    }

    @Test
    void createCandidateReturnsCreatedCandidate() {
        candidateDto inputDto = new candidateDto(null, "John Doe", "john.doe@example.com", "1234567890");
        candidateDto createdDto = new candidateDto(UUID.randomUUID(), "John Doe", "john.doe@example.com", "1234567890");
        when(service.createCandidate(inputDto)).thenReturn(createdDto);

        ResponseEntity<candidateDto> response = controller.createCandidate(inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdDto, response.getBody());
    }

    @Test
    void updateCandidateReturnsUpdatedCandidate() {
        UUID id = UUID.randomUUID();
        candidateDto inputDto = new candidateDto(null, "Jane Doe", "jane.doe@example.com", "0987654321");
        candidateDto updatedDto = new candidateDto(id, "Jane Doe", "jane.doe@example.com", "0987654321");
        when(service.updateCandidate(id, inputDto)).thenReturn(updatedDto);

        ResponseEntity<candidateDto> response = controller.updateCandidate(id, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
    }

    @Test
    void deleteCandidateReturnsNoContentWhenSuccessful() {
        UUID id = UUID.randomUUID();
        doNothing().when(service).deleteCandidate(id);

        ResponseEntity<Void> response = controller.deleteCandidate(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteCandidateThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Candidate not found")).when(service).deleteCandidate(id);

        assertThrows(RuntimeException.class, () -> controller.deleteCandidate(id));
    }
}