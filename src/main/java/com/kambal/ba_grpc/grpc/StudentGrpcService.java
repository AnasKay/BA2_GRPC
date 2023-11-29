package com.kambal.ba_grpc.grpc;

import com.kambal.ba_grpc.GreetRequest;
import com.kambal.ba_grpc.GreetResponse;
import com.kambal.ba_grpc.GreetServiceGrpc;
import com.kambal.ba_grpc.models.Grade;
import com.kambal.ba_grpc.models.Professor;
import com.kambal.ba_grpc.models.Student;
import com.kambal.ba_grpc.service.GradeService;
import com.kambal.ba_grpc.service.ProfessorService;
import com.kambal.ba_grpc.service.StudentService;
import com.kambal.school.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class StudentGrpcService extends StudentServiceGrpc.StudentServiceImplBase {

    @Autowired
    StudentService studentService;

    @Autowired
    ProfessorService professorService;

    @Autowired
    GradeService gradeService;

    @Override
    public void getStudentInfo(StudentByIdRequest request, StreamObserver<StudentByIdResponse> responseObserver) {
        String studentId = request.getStudentId();
        Optional<Student> studentOptional = studentService.getStudent(Long.valueOf(studentId));

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            StudentByIdResponse response = buildStudent(student);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void getStudents(GetStudentsRequest request,StreamObserver<GetStudentsResponse> responseObserver) {
        List<Student> studentList = studentService.getStudents();
        GetStudentsResponse.Builder responseBuilder = GetStudentsResponse.newBuilder();

        for (Student student : studentList) {
            StudentByIdResponse grpcStudent = buildStudent(student);
            responseBuilder.addStudents(grpcStudent);
        }
        GetStudentsResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void createStudent(CreateStudentRequest request, StreamObserver<CreateStudentResponse> responseObserver) {
        String firstname = request.getFirstname();
        String lastname = request.getLastname();
        String emailId = request.getEmailId();
        GradeByIdResponse grade = request.getGrade();

        Grade myGrade = new Grade();
        myGrade.setName(grade.getName());

        Student myStudent = new Student();
        myStudent.setFirstName(firstname);
        myStudent.setLastName(lastname);
        myStudent.setEmailId(emailId);
        myStudent.setGrade(myGrade);


        Student newStudent = studentService.createStudent(myStudent);

        myGrade.setId(newStudent.getGrade().getId());
        myGrade.setMaxStudents(newStudent.getGrade().getMaxStudents());
        myGrade.setProfessor(newStudent.getGrade().getProfessor());

        CreateStudentResponse response = CreateStudentResponse.newBuilder()
                .setStudent(StudentByIdResponse.newBuilder()
                        .setStudentId(newStudent.getId().toString())
                        .setFirstname(newStudent.getFirstName())
                        .setLastname(newStudent.getLastName())
                        .setEmailId(newStudent.getEmailId())
                        .setGrade(buildGrade(myGrade))
                        .build())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateStudent(UpdateStudentRequest request, StreamObserver<StudentByIdResponse> responseObserver) {
        String studentId = request.getStudentId();
        String newFirstname = request.getFirstname();
        String newLastname = request.getLastname();
        String newEmailId = request.getEmailId();

        Student newStudent = new Student();
        newStudent.setFirstName(newFirstname);
        newStudent.setLastName(newLastname);
        newStudent.setEmailId(newEmailId);

        if(studentId!=null){
            newStudent = studentService.updateStudent(Long.valueOf(studentId), newStudent);
        }

        StudentByIdResponse response = buildStudent(newStudent);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteStudent(DeleteStudentRequest request, StreamObserver<DeleteStudentResponse> responseObserver) {
        String studentId = request.getStudentId();

        Student student = new Student();
        student.setEmailId(studentId);
        student = studentService.deleteStudent(student);
        boolean success = false;

        if(student.getLastName()!=null && student.getLastName().length()>0){
            success = true;
        }

        DeleteStudentResponse response = DeleteStudentResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void getProfessorInfo(ProfessorByIdRequest request, StreamObserver<ProfessorByIdResponse> responseObserver) {
        String ProfessorId = request.getProfessorId();
        Professor professor = professorService.getProfessor(Long.valueOf(ProfessorId));
        ProfessorByIdResponse response = buildProfessor(professor);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProfessors(GetProfessorsRequest request,StreamObserver<GetProfessorsResponse> responseObserver) {
        List<Professor> professorList = professorService.getProfessor();
        GetProfessorsResponse.Builder responseBuilder = GetProfessorsResponse.newBuilder();

        for (Professor professor : professorList) {
            ProfessorByIdResponse grpcProfessor = buildProfessor(professor);
            responseBuilder.addProfessors(grpcProfessor);
        }
        GetProfessorsResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void createProfessor(CreateProfessorRequest request, StreamObserver<CreateProfessorResponse> responseObserver) {
        String firstname = request.getFirstname();
        String lastname = request.getLastname();
        String emailId = request.getEmailId();

        Professor myProfessor = new Professor();
        myProfessor.setFirstName(firstname);
        myProfessor.setLastName(lastname);
        myProfessor.setEmailId(emailId);

        Professor newProfessor = professorService.createProfessor(myProfessor);

        CreateProfessorResponse response = CreateProfessorResponse.newBuilder()
                .setProfessor(ProfessorByIdResponse.newBuilder()
                        .setId(newProfessor.getId())
                        .setFirstname(newProfessor.getFirstName())
                        .setLastname(newProfessor.getLastName())
                        .setEmail(newProfessor.getEmailId())
                        .build())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateProfessor(UpdateProfessorRequest request, StreamObserver<ProfessorByIdResponse> responseObserver) {
        String professorId = request.getProfessorId();
        String newFirstname = request.getFirstname();
        String newLastname = request.getLastname();
        String newEmailId = request.getEmailId();

        Professor newProfessor = new Professor();
        newProfessor.setFirstName(newFirstname);
        newProfessor.setLastName(newLastname);
        newProfessor.setEmailId(newEmailId);

        if(professorId!=null){
            newProfessor = professorService.updateProfessor(Long.valueOf(professorId), newProfessor);
        }

        ProfessorByIdResponse response = buildProfessor(newProfessor);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteProfessor(DeleteProfessorRequest request, StreamObserver<DeleteProfessorResponse> responseObserver) {
        String professorId = request.getProfessorId();

        Professor professor = new Professor();
        professor.setEmailId(professorId);
        professor = professorService.deleteProfessor(professor);
        boolean success = false;

        if(professor.getLastName()!=null && professor.getLastName().length()>0){
            success = true;
        }

        DeleteProfessorResponse response = DeleteProfessorResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }



    @Override
    public void getGradeInfo(GradeByIdRequest request, StreamObserver<GradeByIdResponse> responseObserver) {
        String gradeId = request.getGradeId();
        Optional<Grade> gradeOptional = gradeService.getGrade(Long.valueOf(gradeId));

        if (gradeOptional.isPresent()) {
            Grade grade = gradeOptional.get();
            GradeByIdResponse response = buildGrade(grade);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void getGrades(GetGradesRequest request,StreamObserver<GetGradesResponse> responseObserver) {
        List<Grade> gradeList = gradeService.getGrades();
        GetGradesResponse.Builder responseBuilder = GetGradesResponse.newBuilder();

        for (Grade grade : gradeList) {
            GradeByIdResponse grpcGrade = buildGrade(grade);
            responseBuilder.addGrades(grpcGrade);
        }
        GetGradesResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void createGrade(CreateGradeRequest request, StreamObserver<CreateGradeResponse> responseObserver) {
        String name = request.getName();
        Long maxStudents = request.getMaxStudents();
        ProfessorByIdResponse professor = request.getProfessor();

        Professor myProf = new Professor();
        myProf.setEmailId(professor.getEmail());

        Grade myGrade = new Grade();
        myGrade.setName(name);
        myGrade.setMaxStudents(maxStudents.intValue());
        myGrade.setProfessor(myProf);


        Grade newGrade = gradeService.createGrade(myGrade);

        myGrade.setId(newGrade.getId());
        myGrade.setMaxStudents(newGrade.getMaxStudents());
        myGrade.setProfessor(newGrade.getProfessor());

        CreateGradeResponse response = CreateGradeResponse.newBuilder()
                .setGrade(GradeByIdResponse.newBuilder()
                        .setId(newGrade.getId())
                        .setName(newGrade.getName())
                        .setMaxStudents(newGrade.getMaxStudents())
                        .setProfessor(buildProfessor(myProf))
                        .build())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateGrade(UpdateGradeRequest request, StreamObserver<GradeByIdResponse> responseObserver) {
        String gradeId = request.getGradeId();
        String newName = request.getName();
        Long newMaxStudents = request.getMaxStudents();

        Grade newGrade = new Grade();
        newGrade.setName(newName);
        newGrade.setMaxStudents(newMaxStudents.intValue());

        if(gradeId!=null){
            newGrade = gradeService.updateGrade(Long.valueOf(gradeId), newGrade);
        }

        GradeByIdResponse response = buildGrade(newGrade);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteGrade(DeleteGradeRequest request, StreamObserver<DeleteGradeResponse> responseObserver) {
        String gradeId = request.getGradeId();

        Grade grade = new Grade();
        grade.setName(gradeId);
        grade = gradeService.deleteGrade(grade);
        boolean success = false;

        if(grade.getProfessor()!=null){
            success = true;
        }

        DeleteGradeResponse response = DeleteGradeResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    private GradeByIdResponse buildGrade(Grade grade) {
        return GradeByIdResponse.newBuilder()
                .setId(grade.getId())
                .setName(grade.getName())
                .setMaxStudents(grade.getMaxStudents())
                .setProfessor(buildProfessor(grade.getProfessor()))
                .build();
    }
    private ProfessorByIdResponse buildProfessor(Professor professor) {
        return ProfessorByIdResponse.newBuilder()
                .setId(professor.getId())
                .setFirstname(professor.getFirstName())
                .setLastname(professor.getLastName())
                .setEmail(professor.getEmailId())
                .build();
    }

    private StudentByIdResponse buildStudent(Student student){
        return StudentByIdResponse.newBuilder()
                .setStudentId(student.getId().toString())
                .setFirstname(student.getFirstName())
                .setLastname(student.getLastName())
                .setEmailId(student.getEmailId())
                .setGrade(buildGrade(student.getGrade()))
                .build();
    }

}
