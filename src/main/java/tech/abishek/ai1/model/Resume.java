package tech.abishek.ai1.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 5000)
    private String text;
}
