package br.com.palo.ti.candidate.services;

import br.com.palo.ti.candidate.dtos.candidateDto;
import br.com.palo.ti.candidate.dtos.statusDto;
import br.com.palo.ti.candidate.models.candidateModel;
import br.com.palo.ti.candidate.repositories.candidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class candidateService {

    @Autowired
    private candidateRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    /**
     * Creates a new candidate in the database.
     *
     * @param dto The candidate DTO containing the details of the candidate to be created.
     * @return The created candidate as a DTO.
     */
    public candidateDto createCandidate(candidateDto dto) {
        candidateModel model = convertToModel(dto);
        candidateModel savedModel = repository.save(model);
        return convertToDto(savedModel);
    }

    /**
     * Retrieves all candidates from the database.
     *
     * @return A list of candidate DTOs.
     */
    public List<candidateDto> getAllCandidates() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a candidate by their unique identifier.
     *
     * @param id The UUID of the candidate to retrieve.
     * @return The candidate DTO if found.
     * @throws RuntimeException if the candidate is not found.
     */
    public candidateDto getCandidateById(UUID id) {
        candidateModel model = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        return convertToDto(model);
    }

    /**
     * Updates an existing candidate's details.
     *
     * @param id The UUID of the candidate to update.
     * @param candidateDto The candidate DTO containing the updated details.
     * @return The updated candidate as a DTO.
     * @throws RuntimeException if the candidate is not found.
     */
    public candidateDto updateCandidate(UUID id, candidateDto candidateDto) {
        candidateModel existingModel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (candidateDto.name() != null) {
            existingModel.setName(candidateDto.name());
        }
        if (candidateDto.email() != null) {
            existingModel.setEmail(candidateDto.email());
        }
        if (candidateDto.phone() != null) {
            existingModel.setPhone(candidateDto.phone());
        }

        candidateModel updatedModel = repository.save(existingModel);
        return convertToDto(updatedModel);
    }

    /**
     * Deletes a candidate by their unique identifier.
     *
     * @param id The UUID of the candidate to delete.
     * @return void
     */
    public void deleteCandidate(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Proposal not found");
        }
        repository.deleteById(id);
    }

    /**
     * Updates the status of a candidate and sends an email notification.
     *
     * @param statusDto The status DTO containing the candidate's email and status.
     */
    public void updateStatusToCandidate(statusDto statusDto) {
        candidateModel model = repository.findByEmail(statusDto.email())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        sendEmail(model.getEmail().toString(), environment.getProperty(statusDto.status()), environment.getProperty(statusDto.status()+".message"));
    }


    /**
     * Sends an email notification to the candidate.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param text    The body of the email.
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }


    /**
     * Converts a candidate model to a DTO.
     *
     * @param model The candidate model.
     * @return The candidate DTO.
     */
    private candidateDto convertToDto(candidateModel model) {
        return new candidateDto(
                model.getId(),
                model.getName(),
                model.getEmail(),
                model.getPhone()
        );
    }

    /**
     * Converts a candidate DTO to a model.
     *
     * @param dto The candidate DTO.
     * @return The candidate model.
     */
    private candidateModel convertToModel(candidateDto dto) {
        candidateModel model = new candidateModel();
        model.setName(dto.name());
        model.setEmail(dto.email());
        model.setPhone(dto.phone());
        return model;
    }
}