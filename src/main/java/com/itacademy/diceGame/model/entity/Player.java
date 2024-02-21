package com.itacademy.diceGame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "players", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Timestamp registration_date;

    public Player(String name){
        this.name = WordUtils.capitalize(name);
    }

}
