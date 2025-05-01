package com.github.Garden.Entities;

import com.github.Garden.Tree.LeafType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tree_table")
@EntityListeners(AuditingEntityListener.class)
public class Tree extends PhysicEntity {

    @Column(name = "specie_name")
    private String specie;

    @Column(name = "tree_height")
    private Double height;  // like 3m, Integer wrapper to init as null

    @Column(name = "tree_age")
    private Integer age;

    @Column(name = "leaf_type")
    @Enumerated(EnumType.STRING)
    private LeafType leafType;

    @Column(name = "creation_date")
    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_actualization")
    private LocalDateTime lastActualization;

    public Tree() {}

    public Tree(String specie, Double height, Integer age, LeafType leafType) {
        this.specie = specie;
        this.height = height;
        this.age = age;
        this.leafType = leafType;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastActualization() {
        return lastActualization;
    }

    public void setLastActualization(LocalDateTime lastActualization) {
        this.lastActualization = lastActualization;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LeafType getLeafType() {
        return leafType;
    }

    public void setLeafType(LeafType leafType) {
        this.leafType = leafType;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
