package com.gah.draft.database.repository;

import com.gah.draft.database.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    List<Player> findAll();

    List<Player> findAllByPosition(String position);

    Player findByPlayerName(String playerName);

    Player findByPick(int pickNumber);

    List<Player> findAllBySelected(boolean selected);
}
