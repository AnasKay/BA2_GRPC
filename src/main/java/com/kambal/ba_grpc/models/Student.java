package com.kambal.ba_grpc.models;

import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stud_id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email_id")
    private String emailId;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "isInGrade", referencedColumnName = "grade_id")
    private Grade grade;
*/
    @ManyToOne
    @JoinColumn(name="grade_id", nullable=false)
    private Grade grade;

}
