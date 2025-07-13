package com.example.msrequests.repository;

import com.example.msrequests.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    // Puedes añadir métodos de consulta personalizados si los necesitas, por ejemplo:
    List<Request> findByUserId(Long userId);
}
