package com.example.traning_management.entity;

import com.example.traning_management.entity.enums.NominationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "nominations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_officer_training",
                        columnNames = {
                                "officer_id",
                                "training_program_id"
                        }
                )
        }
)
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private Officer officer;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "training_program_id", nullable = false)
    private TrainingProgram trainingProgram;

    private LocalDateTime nominatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NominationStatus status;

}