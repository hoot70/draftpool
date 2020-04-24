package com.gah.draft.database.service;

import com.gah.draft.database.model.Selection;
import com.gah.draft.database.repository.SelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectionService {
    @Autowired
    private SelectionRepository selectionRepository;

    public List<Selection> getAllBySelector(int selector){
        return selectionRepository.findBySelector(selector);
    }

    public List<Selection> getAllByPlayerName(String playerName){
        return selectionRepository.findAllByPlayerName(playerName);
    }

    public Selection getBySelectorAndPick(int selector, int pick){
        return selectionRepository.findBySelectorAndPick(selector, pick);
    }

    public Selection getBySelectorAndPlayerName(int selector, String playerName){
        return selectionRepository.findBySelectorAndAndPlayerName(selector, playerName);
    }

    public List<Selection> getAllByPick(int pick){
        return selectionRepository.findAllByPick(pick);
    }

    public void save(Selection selection){
        selectionRepository.save(selection);
    }

    public void update(Selection selection){
        selectionRepository.save(selection);
    }

    public void delete(Selection selection){selectionRepository.delete(selection);}

}
