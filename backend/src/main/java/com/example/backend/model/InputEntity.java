package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Input")
public class InputEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inputId;

    private String input; //input 내용

}