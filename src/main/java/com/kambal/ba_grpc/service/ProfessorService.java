package com.kambal.ba_grpc.service;

import com.kambal.ba_grpc.models.Professor;
import com.kambal.ba_grpc.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {
    @Autowired
    ProfessorRepository professorRepository;

    public Professor createProfessor(Professor professor){
        return professorRepository.save(professor);
    }

    public List<Professor> getProfessor() {
        return professorRepository.findAll();
    }

    public Professor getProfessor(Long professorId){
        return professorRepository.findProfessorById(professorId);
    }

    public Professor getProfessorByEmailId(String emailId){
        return professorRepository.findProfessorByEmailId(emailId);
    }



    public List<Professor> getProfessorsByLastName(String lastName){
        return professorRepository.findProfessorsByLastName(lastName);
    }

    public void deleteProfessorById(Long professorId) {
        if(professorRepository.existsById(professorId)){
            professorRepository.deleteById(professorId);
        }
    }

    public Professor updateProfessor(Long professorId, Professor professor) {
        Professor professor1 = new Professor();

        if(professorRepository.findById(professorId).isPresent()) {
            professor1 = professorRepository.findById(professorId).get();

            if (!(professor.getFirstName() == null)){
                professor1.setFirstName(professor.getFirstName());
            }
            if (!(professor.getLastName() == null)){
                professor1.setLastName(professor.getLastName());
            }
            if (!(professor.getEmailId() == null)){
                professor1.setEmailId(professor.getEmailId());
            }

            return professorRepository.save(professor1);
        }

        return professor1;
    }

    public Professor getProfByValue(Professor professor){
        if(professor.getId()!= null){
            professor = getProfessor(professor.getId());
        } else if (professor.getLastName()!= null) {
            professor = getProfessorsByLastName(professor.getLastName()).get(0);
        } else if (professor.getEmailId()!= null) {
            professor = getProfessorByEmailId(professor.getEmailId());
        }
        return professor;
    }

    public Professor deleteProfessor(Professor professor) {
        professor = getProfByValue(professor);
        professorRepository.delete(professor);
        return professor;
    }
}
