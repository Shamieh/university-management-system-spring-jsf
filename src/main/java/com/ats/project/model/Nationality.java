package com.ats.project.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nationality")
@Getter
@Setter
public class Nationality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Override
    public String toString() {
        return "Nationality{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
