package com.itacademy.diceGame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@Table(name = "players")
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(50) default 'ANONYMOUS'")
    private String name;
    @Column(columnDefinition = "datetime default current_timestamp")
    @CreationTimestamp(source = SourceType.DB)
    private LocalDate registration_date;
    private Double successRate;

    public Player(String name){
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
    }

    public void setName(String name) {
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
    }
}
