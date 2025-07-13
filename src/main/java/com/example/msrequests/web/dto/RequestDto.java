package com.example.msrequests.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluirá campos nulos en la respuesta JSON
public class RequestDto {

    private Long id;
    private Long userId;
    private Long medicationId;
    private Integer quantity;
    private String status;
    private LocalDateTime createdAt;

    // Nuevos campos para NO POS
    private String orderNumber;
    private String address;
    private String phone;
    private String email;

    private MedicationDto medication;
}