package com.example.msrequests.service;

import com.example.msrequests.domain.Medication;
import com.example.msrequests.domain.Request;
import com.example.msrequests.exception.BadRequestException;
import com.example.msrequests.exception.ResourceNotFoundException;
import com.example.msrequests.repository.MedicationRepository;
import com.example.msrequests.repository.RequestRepository;
import com.example.msrequests.web.dto.MedicationDto;
import com.example.msrequests.web.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final MedicationRepository medicationRepository;

    @Transactional
    public RequestDto createRequest(RequestDto requestDto) {
        Medication medication = medicationRepository.findById(requestDto.getMedicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + requestDto.getMedicationId()));

        Request request = new Request();
        request.setUserId(requestDto.getUserId());
        request.setMedication(medication);
        request.setQuantity(requestDto.getQuantity());

        if (medication.isNoPos()) {
            if (requestDto.getOrderNumber() == null || requestDto.getOrderNumber().isBlank() ||
                requestDto.getAddress() == null || requestDto.getAddress().isBlank() ||
                requestDto.getPhone() == null || requestDto.getPhone().isBlank() ||
                requestDto.getEmail() == null || requestDto.getEmail().isBlank()) {
                throw new BadRequestException("Order number, address, phone, and email are required for NO POS medications.");
            }
            request.setOrderNumber(requestDto.getOrderNumber());
            request.setAddress(requestDto.getAddress());
            request.setPhone(requestDto.getPhone());
            request.setEmail(requestDto.getEmail());
        }

        Request savedRequest = requestRepository.save(request);
        return convertToDto(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByUserId(Long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RequestDto> getAllRequests(Pageable pageable) {
        Page<Request> requestPage = requestRepository.findAll(pageable);
        return requestPage.map(this::convertToDto);
    }

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
                medicationDto
        );
    }
}
