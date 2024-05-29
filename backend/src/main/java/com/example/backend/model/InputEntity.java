package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private LocalDate date;
    private String Content;
}