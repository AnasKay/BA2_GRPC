package com.kambal.ba_grpc.models;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "professor")
@Data
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prof_id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email_id")
    private String emailId;

   /* @OneToOne(mappedBy = "professor")
    private Grade grade;*/

}
