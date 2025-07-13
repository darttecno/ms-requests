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

        // 2. Crear la entidad Request y mapear campos básicos
        Request request = new Request();
        request.setUserId(requestDto.getUserId());
        request.setMedication(medication);
        request.setQuantity(requestDto.getQuantity());

        // 3. Lógica de validación para medicamentos NO POS
        if (medication.isNoPos()) {
            // Si es NO POS, los campos adicionales son obligatorios
            if (requestDto.getOrderNumber() == null || requestDto.getOrderNumber().isBlank() ||
                requestDto.getAddress() == null || requestDto.getAddress().isBlank() ||
                requestDto.getPhone() == null || requestDto.getPhone().isBlank() ||
                requestDto.getEmail() == null || requestDto.getEmail().isBlank()) {
                throw new IllegalArgumentException("Order number, address, phone, and email are required for NO POS medications.");
            }
            // Mapear los campos adicionales a la entidad
            request.setOrderNumber(requestDto.getOrderNumber());
            request.setAddress(requestDto.getAddress());
            request.setPhone(requestDto.getPhone());
            request.setEmail(requestDto.getEmail());
        }

        // 4. Guardar en la base de datos (el estado y la fecha se asignan con @PrePersist)
        Request savedRequest = requestRepository.save(request);

        // 5. Convertir a DTO para la respuesta
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
                request.getOrderNumber(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail(),
                medicationDto // Devolvemos la info de la medicación anidada
        );
    }
}
