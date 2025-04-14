package br.com.palo.ti.candidate.services;

import br.com.palo.ti.candidate.dtos.candidateDto;
import br.com.palo.ti.candidate.models.candidateModel;
import br.com.palo.ti.candidate.repositories.candidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class candidateServiceTest {

    @Mock
    private candidateRepository repository;

    @Mock
    private candidateModel model1;

    @Mock
    private candidateModel model2;

    @InjectMocks
    private candidateService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCandidatesReturnsListOfCandidates() {
        List<candidateModel> models = List.of(model1, model2);
        when(repository.findAll()).thenReturn(models);
        when(model1.getName()).thenReturn("John Doe");
        when(model2.getName()).thenReturn("Jane Doe");

        List<candidateDto> result = service.getAllCandidates();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).name());
        assertEquals("Jane Doe", result.get(1).name());
    }

    @Test
    void getCandidateByIdReturnsCandidateWhenFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(model1));
        when(model1.getName()).thenReturn("John Doe");
        when(model1.getEmail()).thenReturn("john.doe@example.com");

        candidateDto result = service.getCandidateById(id);

        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
    }

    @Test
    void getCandidateByIdThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getCandidateById(id));
    }

    @Test
    void createCandidateSavesAndReturnsCandidate() {
        candidateDto dto = new candidateDto(null, "John Doe", "john.doe@example.com", "1234567890");
        candidateModel savedModel = model1;
        when(repository.save(any(candidateModel.class))).thenReturn(savedModel);
        when(savedModel.getName()).thenReturn("John Doe");
        when(savedModel.getEmail()).thenReturn("john.doe@example.com");

        candidateDto result = service.createCandidate(dto);

        verify(repository).save(any(candidateModel.class));
        assertEquals("John Doe", result.name());
        assertEquals("john.doe@example.com", result.email());
    }

    @Test
    void updateCandidateUpdatesAndReturnsCandidate() {
        UUID id = UUID.randomUUID();
        candidateDto dto = new candidateDto(null, "Jane Doe", "jane.doe@example.com", "0987654321");
        candidateModel updatedModel = mock(candidateModel.class);

        when(updatedModel.getId()).thenReturn(id);
        when(updatedModel.getName()).thenReturn("Jane Doe");
        when(updatedModel.getEmail()).thenReturn("jane.doe@example.com");

        when(repository.findById(id)).thenReturn(Optional.of(model1));
        when(repository.save(model1)).thenReturn(updatedModel);

        candidateDto result = service.updateCandidate(id, dto);

        assertEquals("Jane Doe", result.name());
        assertEquals("jane.doe@example.com", result.email());
    }

    @Test
    void updateCandidateThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        candidateDto dto = new candidateDto(null, "Jane Doe", "jane.doe@example.com", "0987654321");
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateCandidate(id, dto));
    }

    @Test
    void deleteCandidateDeletesWhenFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        assertDoesNotThrow(() -> service.deleteCandidate(id));
    }

    @Test
    void deleteCandidateThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.deleteCandidate(id));
    }
}