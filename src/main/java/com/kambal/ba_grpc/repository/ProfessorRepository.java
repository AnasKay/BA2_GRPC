package com.kambal.ba_grpc.repository;

import com.kambal.ba_grpc.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Professor findProfessorById(Long id);
    Professor findProfessorByEmailId(String emailId);
    List<Professor> findProfessorsByLastName(String lastName);
}
