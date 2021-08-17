package com.gah.draft.database.service;

import com.gah.draft.database.model.Player;
import com.gah.draft.database.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAll(){
        return playerRepository.findAll();
    }

    public List<Player> getAllByPosition(String position){
        return playerRepository.findAllByPosition(position);
    }

    public List<Player> getAllBySelected(boolean selected){
        return playerRepository.findAllBySelected(selected);
    }

    public List<Player> getAllBySelectedAndAndPick(boolean selected, int pickNumber) {return playerRepository.findAllBySelectedAndAndPick(selected, pickNumber);}

    public Player getPlayerByPick(int pickNumber){
        return playerRepository.findByPick(pickNumber);
    }

    public Player getPlayerByPlayerName(String playerName){
        return playerRepository.findByPlayerName(playerName);
    }

    public void save(Player player){
        playerRepository.save(player);
    }

    public void delete(String playerName){
        playerRepository.delete(playerRepository.findByPlayerName(playerName));
    }

    public void update(Player player){
        playerRepository.save(player);
    }
}
