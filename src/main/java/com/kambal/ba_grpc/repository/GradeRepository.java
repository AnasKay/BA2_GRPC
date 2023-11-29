package com.kambal.ba_grpc.repository;

import com.kambal.ba_grpc.models.Grade;
import com.kambal.ba_grpc.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findGradeByProfessor(Professor kv);
    Optional<Grade> findGradeByName(String name);
    Grade getGradeById(Long id);
}
