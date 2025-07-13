package com.example.msrequests.service;

import com.example.msrequests.domain.Medication;
import com.example.msrequests.domain.Request;
import com.example.msrequests.repository.MedicationRepository;
import com.example.msrequests.repository.RequestRepository;
import com.example.msrequests.web.dto.MedicationDto;
import com.example.msrequests.web.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final MedicationRepository medicationRepository; // Necesitamos este repo para buscar la medicación

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        // 1. Validar que la medicación existe
        Medication medication = medicationRepository.findById(requestDto.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + requestDto.getMedicationId()));

        // Aquí es donde harías la llamada al microservicio de usuarios para validar el userId si fuera necesario.
        // Por ahora, confiamos en el ID que nos llega.

        // 2. Crear la entidad Request
        Request request = new Request();
        request.setUserId(requestDto.getUserId());
        request.setMedication(medication);
        request.setQuantity(requestDto.getQuantity());
        // El estado y la fecha se asignan con @PrePersist

        // 3. Guardar en la base de datos
        Request savedRequest = requestRepository.save(request);

        // 4. Convertir a DTO para la respuesta
        return convertToDto(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByUserId(Long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // --- Métodos de conversión ---

    private RequestDto convertToDto(Request request) {
        MedicationDto medicationDto = new MedicationDto(
                request.getMedication().getId(),
                request.getMedication().getName(),
                request.getMedication().getDescription(),
                request.getMedication().getAtcCode(),
                request.getMedication().getManufacturer(),
                request.getMedication().isNoPos(),
                request.getMedication().getCreatedAt()
        );

        return new RequestDto(
                request.getId(),
                request.getUserId(),
                request.getMedication().getId(),
                request.getQuantity(),
                request.getStatus(),
                request.getCreatedAt(),
                medicationDto // Devolvemos la info de la medicación anidada
        );
    }
}
