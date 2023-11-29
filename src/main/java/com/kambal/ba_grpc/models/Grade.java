package com.kambal.ba_grpc.models;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "grade")
@Data
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grade_id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="maxStudents")
    private Integer maxStudents;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kv", referencedColumnName = "prof_id")
    private Professor professor;

    /*@OneToOne(mappedBy = "grade")
    private Student student;*/

    /*@OneToMany(mappedBy="grade")
    private Set<Student> students;*/

}
