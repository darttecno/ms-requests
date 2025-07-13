package com.example.msrequests.service;

import com.example.msrequests.domain.Medication;
import com.example.msrequests.repository.MedicationRepository;
import com.example.msrequests.web.dto.MedicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Transactional(readOnly = true)
    public List<MedicationDto> getAllMedications() {
        return medicationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicationDto getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Medication not found with id " + id));
    }

    @Transactional
    public MedicationDto createMedication(MedicationDto medicationDto) {
        Medication medication = convertToEntity(medicationDto);
        Medication savedMedication = medicationRepository.save(medication);
        return convertToDto(savedMedication);
    }

    @Transactional
    public MedicationDto updateMedication(Long id, MedicationDto medicationDto) {
        Medication existingMedication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id " + id));

        existingMedication.setName(medicationDto.getName());
        existingMedication.setDescription(medicationDto.getDescription());
        existingMedication.setAtcCode(medicationDto.getAtcCode());
        existingMedication.setManufacturer(medicationDto.getManufacturer());
        existingMedication.setNoPos(medicationDto.isNoPos());

        Medication updatedMedication = medicationRepository.save(existingMedication);
        return convertToDto(updatedMedication);
    }

    @Transactional
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Medication not found with id " + id);
        }
        medicationRepository.deleteById(id);
    }

    private MedicationDto convertToDto(Medication medication) {
        return new MedicationDto(
                medication.getId(),
                medication.getName(),
                medication.getDescription(),
                medication.getAtcCode(),
                medication.getManufacturer(),
                medication.isNoPos(),
                medication.getCreatedAt()
        );
    }

    private Medication convertToEntity(MedicationDto medicationDto) {
        Medication medication = new Medication();
        medication.setName(medicationDto.getName());
        medication.setDescription(medicationDto.getDescription());
        medication.setAtcCode(medicationDto.getAtcCode());
        medication.setManufacturer(medicationDto.getManufacturer());
        medication.setNoPos(medicationDto.isNoPos());
        return medication;
    }
}
