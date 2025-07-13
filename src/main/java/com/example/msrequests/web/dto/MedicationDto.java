package com.example.msrequests.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDto {

    private Long id;
    private String name;
    private String description;
    private String atcCode;
    private String manufacturer;
    private boolean isNoPos;
    private LocalDateTime createdAt;
}
