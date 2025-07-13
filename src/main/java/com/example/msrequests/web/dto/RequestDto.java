package com.example.msrequests.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Long id;
    private Long userId;
    private Long medicationId;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;

    // Opcional: puedes añadir un DTO para la medicación si quieres devolverla anidada
    private MedicationDto medication;
}
