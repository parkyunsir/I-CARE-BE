package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Question")
public class QuestionEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid")
    private String questionId;

    private String parentId;

    private LocalDate date;
    private String input; //질문 ex) 가장 좋아하는 놀이가 뭐니?
    private String output; //답변 ex) 나는 소꿉놀이가 제일 좋아
}
