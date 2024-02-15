package com.itacademy.diceGame.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@Document(collection = "players")
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {
    @Id
    private String id;
    private String name;
    private LocalDate registration_date;
    private Double successRate;

    public Player(String name){
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
    }

    public void setName(String name) {
        this.name = WordUtils.capitalize(Objects.requireNonNullElse(name, "ANONYMOUS"));
    }
}
