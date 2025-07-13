package com.example.msrequests.web.controller;

import com.example.msrequests.service.RequestService;
import com.example.msrequests.web.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@RequestBody RequestDto requestDto,
                                                    @RequestHeader("X-User-Id") Long userId) {
        // Asignamos el userId extraído de la cabecera al DTO.
        // El frontend ya no necesita enviarlo en el JSON.
        requestDto.setUserId(userId);

        RequestDto createdRequest = requestService.createRequest(requestDto);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestDto>> getRequestsByUserId(@PathVariable Long userId) {
        List<RequestDto> requests = requestService.getRequestsByUserId(userId);
        return ResponseEntity.ok(requests);
    }
}
