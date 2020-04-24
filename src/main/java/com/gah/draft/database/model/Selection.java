package com.gah.draft.database.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Selection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @CsvBindByName
    private int pick;

    @CsvBindByName
    private String playerName;

    @CsvBindByName
    private String team;

    private String position;

    private int selector;

    private int score;

    private boolean correct;

    private boolean tradeCorrect;

    private boolean tradeAndTeamCorrect;

    private boolean tradeOccurred;

    private boolean positionCorrect;

    private boolean failedTrade;
}
