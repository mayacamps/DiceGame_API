package com.itacademy.diceGame.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Entity
@Data
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int dice1;
    @Column(nullable = false)
    private int dice2;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Player player;

    public Game(int dice1, int dice2, Player player){
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.player = player;
    }

}
