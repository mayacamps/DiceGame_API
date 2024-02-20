package com.itacademy.diceGame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@Table(name = "players")
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @CreationTimestamp(source = SourceType.DB)
    private Timestamp registration_date;

    public Player(String name){
        this.name =  WordUtils.capitalize(StringUtils.defaultIfBlank(name,"ANONYMOUS"));
    }

    public void setName(String name){
        this.name =  WordUtils.capitalize(StringUtils.defaultIfBlank(name,"ANONYMOUS"));
    }
}
