package com.kambal.ba_grpc.service;

import com.kambal.ba_grpc.models.Student;
import com.kambal.ba_grpc.repository.GradeRepository;
import com.kambal.ba_grpc.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GradeRepository gradeRepository;

    public Student createStudent(Student student){
        student.setGrade(gradeRepository.findGradeByName(student.getGrade().getName()).get());
        return studentRepository.save(student);
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudent(Long studentId){
        return studentRepository.findById(studentId);
    }

    public Student getStudentByEmailId(String emailId){
        return studentRepository.findStudentByEmailId(emailId);
    }

    public List<Student> getStudentByLastName(String lastName){
        return  studentRepository.findStudentByLastName(lastName);
    }

    public void deleteStudentById(Long studentId) {
        if(studentRepository.existsById(studentId)){
            studentRepository.deleteById(studentId);
        }
    }

    public Student updateStudent(Long studentId, Student student) {
        Student student1 = new Student();

        if(studentRepository.findById(studentId).isPresent()) {
           student1 = studentRepository.findById(studentId).get();

            if (!(student.getFirstName() == null) && student.getFirstName().length()>0){
                student1.setFirstName(student.getFirstName());
            }
            if (!(student.getLastName() == null) && student.getLastName().length()>0){
                student1.setLastName(student.getLastName());
            }
            if (!(student.getEmailId() == null) && student.getEmailId().length()>0){
                student1.setEmailId(student.getEmailId());
            }

            return studentRepository.save(student1);
        }

        return student1;
    }

    public Student getStudentByValue(Student student){
        if(student.getId()!= null){
            student = getStudent(student.getId()).get();
        } else if (student.getLastName()!= null) {
            student = getStudentByLastName(student.getLastName()).get(0);
        } else if (student.getEmailId()!= null) {
            student = getStudentByEmailId(student.getEmailId());
        }
        return student;
    }

    public Student deleteStudent(Student student) {
        student = getStudentByValue(student);
        studentRepository.delete(student);
        return student;
    }

}
