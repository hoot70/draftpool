package com.gah.draft.services.model;

import com.gah.draft.database.model.Selection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DisplayEntry {
    private String name;

    private int score;

    private int correct;

    private int tradeAndTeamCorrect;

    private int tradeCorrect;

    private int positionCorrect;

    private int failedTrade;

    private int tradeOccurred;

    private List<Selection> selections;

    private int rank;

    private int projectedScore;
}
