package com.kambal.ba_grpc.service;

import com.kambal.ba_grpc.models.Grade;
import com.kambal.ba_grpc.models.Professor;
import com.kambal.ba_grpc.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {
    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    ProfessorService professorService;

    public Grade createGrade(Grade grade){
        return gradeRepository.save(grade);
    }

    public List<Grade> getGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getGrade(Long gradeId){
        return gradeRepository.findById(gradeId);
    }

    public Optional<Grade> getGradeByProfessor(Professor professor){
        professor = professorService.getProfByValue(professor);
        return gradeRepository.findGradeByProfessor(professor);
    }

    public Optional<Grade> getGradeByName(String name){
        return gradeRepository.findGradeByName(name);
    }

    public void deleteGradeByID(Long gradeId) {
        if(gradeRepository.existsById(gradeId)){
            gradeRepository.deleteById(gradeId);
        }
    }

    public Grade updateGrade(Long gradeId, Grade grade) {
        Grade grade1 = new Grade();

        if(gradeRepository.findById(gradeId).isPresent()) {
            grade1 = gradeRepository.findById(gradeId).get();

            if (!(grade.getName() == null)){
                grade1.setName(grade.getName());
            }
            if (!(grade.getProfessor() == null)){
                grade1.setProfessor(grade.getProfessor());
            }
            if (!(grade.getMaxStudents() == null)){
                grade1.setMaxStudents(grade.getMaxStudents());
            }

            return gradeRepository.save(grade1);
        }

        return grade1;
    }

    public Grade getGradeByValue(Grade grade){
        if(grade.getId()!= null){
            grade = gradeRepository.findById(grade.getId()).get();
        } else if (grade.getName()!= null) {
            grade = gradeRepository.findGradeByName(grade.getName()).get();
        }
        return grade;
    }

    public Grade deleteGrade(Grade grade) {
        grade = getGradeByValue(grade);
        gradeRepository.delete(grade);
        return grade;
    }

}
