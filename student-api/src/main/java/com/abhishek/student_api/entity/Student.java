package com.abhishek.student_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity // telss that this java class represent a table in the database without this spring has no idea what to do with this class
@Table(name = "students") // tells spring what to name the tables in postgreSql if skip spring uses the class name by default
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    
    @Id // marks the Id field as that primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // tells the postgreSQL to auto-increment the id, so whenever we save student it will increment the id
    private Long id;
    private String name;
    private String email;
    private String course;


    
}
