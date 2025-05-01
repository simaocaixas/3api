package com.github.Garden.entities;

import com.github.Garden.domain.PositionedEntity;
import com.github.Garden.domain.LeafType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tree_table")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tree extends PositionedEntity {

    @Column(name = "specie_name", nullable = false)
    private String specie;

    @Column(name = "tree_height", nullable = false)
    private Double height;

    @Column(name = "tree_age", nullable = false)
    private Integer age;

    @Column(name = "leaf_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeafType leafType;

    @CreatedDate
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_actualization")
    private LocalDateTime lastActualization;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
