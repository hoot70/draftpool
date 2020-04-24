package com.gah.draft.database.repository;

import com.gah.draft.database.model.Selection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SelectionRepository extends CrudRepository<Selection, Integer> {
    List<Selection> findBySelector(int selector);

    Selection findBySelectorAndPick(int selector, int pick);

    List<Selection> findAllByPlayerName(String playerName);

    List<Selection> findAllByPick(int pick);

    Selection findBySelectorAndAndPlayerName(int selector, String playerName);
}
