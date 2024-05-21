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
@Table(name="Icon")
public class IconEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iconId;
    private String font;
    private String name;
}
