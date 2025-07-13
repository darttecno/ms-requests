package com.example.msrequests.service;

import com.example.msrequests.domain.Medication;
import com.example.msrequests.domain.Request;
import com.example.msrequests.exception.BadRequestException;
import com.example.msrequests.exception.ResourceNotFoundException;
import com.example.msrequests.repository.MedicationRepository;
import com.example.msrequests.repository.RequestRepository;
import com.example.msrequests.web.dto.MedicationDto;
import com.example.msrequests.web.dto.RequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_Success_NonNoPos() {
        // Given
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Paracetamol");
        medication.setNoPos(false);

        RequestDto requestDto = new RequestDto();
        requestDto.setMedicationId(1L);
        requestDto.setQuantity(2);
        requestDto.setUserId(100L);

        Request request = new Request();
        request.setId(1L);
        request.setMedication(medication);
        request.setQuantity(2);
        request.setUserId(100L);
        request.setCreatedAt(LocalDateTime.now());

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        // When
        RequestDto createdRequestDto = requestService.createRequest(requestDto);

        // Then
        assertNotNull(createdRequestDto);
        assertEquals(request.getId(), createdRequestDto.getId());
        assertEquals(request.getUserId(), createdRequestDto.getUserId());
        assertEquals(request.getQuantity(), createdRequestDto.getQuantity());
        assertEquals(request.getMedication().getId(), createdRequestDto.getMedicationId());
        verify(medicationRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void createRequest_Success_NoPos() {
        // Given
        Medication medication = new Medication();
        medication.setId(2L);
        medication.setName("Special Drug");
        medication.setNoPos(true);

        RequestDto requestDto = new RequestDto();
        requestDto.setMedicationId(2L);
        requestDto.setQuantity(1);
        requestDto.setUserId(101L);
        requestDto.setOrderNumber("ORD123");
        requestDto.setAddress("123 Main St");
        requestDto.setPhone("555-1234");
        requestDto.setEmail("test@example.com");

        Request request = new Request();
        request.setId(2L);
        request.setMedication(medication);
        request.setQuantity(1);
        request.setUserId(101L);
        request.setOrderNumber("ORD123");
        request.setAddress("123 Main St");
        request.setPhone("555-1234");
        request.setEmail("test@example.com");
        request.setCreatedAt(LocalDateTime.now());

        when(medicationRepository.findById(2L)).thenReturn(Optional.of(medication));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        // When
        RequestDto createdRequestDto = requestService.createRequest(requestDto);

        // Then
        assertNotNull(createdRequestDto);
        assertEquals(request.getId(), createdRequestDto.getId());
        assertEquals(request.getUserId(), createdRequestDto.getUserId());
        assertEquals(request.getOrderNumber(), createdRequestDto.getOrderNumber());
        verify(medicationRepository, times(1)).findById(2L);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void createRequest_NoPos_MissingRequiredFields() {
        // Given
        Medication medication = new Medication();
        medication.setId(3L);
        medication.setName("Another Special Drug");
        medication.setNoPos(true);

        RequestDto requestDto = new RequestDto();
        requestDto.setMedicationId(3L);
        requestDto.setQuantity(1);
        requestDto.setUserId(102L);
        // Missing orderNumber, address, phone, email

        when(medicationRepository.findById(3L)).thenReturn(Optional.of(medication));

        // When & Then
        assertThrows(BadRequestException.class, () -> requestService.createRequest(requestDto));
        verify(medicationRepository, times(1)).findById(3L);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void createRequest_MedicationNotFound() {
        // Given
        RequestDto requestDto = new RequestDto();
        requestDto.setMedicationId(99L);
        requestDto.setQuantity(2);
        requestDto.setUserId(100L);

        when(medicationRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> requestService.createRequest(requestDto));
        verify(medicationRepository, times(1)).findById(99L);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void getRequestsByUserId_Found() {
        // Given
        Long userId = 1L;
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Med1");

        Request request1 = new Request();
        request1.setId(1L);
        request1.setUserId(userId);
        request1.setMedication(medication);
        request1.setQuantity(1);
        request1.setCreatedAt(LocalDateTime.now());

        Request request2 = new Request();
        request2.setId(2L);
        request2.setUserId(userId);
        request2.setMedication(medication);
        request2.setQuantity(2);
        request2.setCreatedAt(LocalDateTime.now());

        List<Request> requests = Arrays.asList(request1, request2);
        when(requestRepository.findByUserId(userId)).thenReturn(requests);

        // When
        List<RequestDto> foundRequests = requestService.getRequestsByUserId(userId);

        // Then
        assertNotNull(foundRequests);
        assertEquals(2, foundRequests.size());
        assertEquals(request1.getId(), foundRequests.get(0).getId());
        assertEquals(request2.getId(), foundRequests.get(1).getId());
        verify(requestRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getRequestsByUserId_NotFound() {
        // Given
        Long userId = 99L;
        when(requestRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<RequestDto> foundRequests = requestService.getRequestsByUserId(userId);

        // Then
        assertNotNull(foundRequests);
        assertTrue(foundRequests.isEmpty());
        verify(requestRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAllRequests_ReturnsPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Med1");

        Request request1 = new Request();
        request1.setId(1L);
        request1.setUserId(1L);
        request1.setMedication(medication);
        request1.setQuantity(1);
        request1.setCreatedAt(LocalDateTime.now());

        Request request2 = new Request();
        request2.setId(2L);
        request2.setUserId(1L);
        request2.setMedication(medication);
        request2.setQuantity(2);
        request2.setCreatedAt(LocalDateTime.now());

        List<Request> requests = Arrays.asList(request1, request2);
        Page<Request> requestPage = new PageImpl<>(requests, pageable, requests.size());

        when(requestRepository.findAll(pageable)).thenReturn(requestPage);

        // When
        Page<RequestDto> resultPage = requestService.getAllRequests(pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(2, resultPage.getTotalElements());
        assertEquals(1, resultPage.getTotalPages());
        assertEquals(request1.getId(), resultPage.getContent().get(0).getId());
        verify(requestRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllRequests_ReturnsEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Request> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(requestRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<RequestDto> resultPage = requestService.getAllRequests(pageable);

        // Then
        assertNotNull(resultPage);
        assertTrue(resultPage.isEmpty());
        verify(requestRepository, times(1)).findAll(pageable);
    }
}