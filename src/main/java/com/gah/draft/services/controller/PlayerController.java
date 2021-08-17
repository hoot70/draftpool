package com.gah.draft.services.controller;

import com.gah.draft.database.model.Entry;
import com.gah.draft.database.model.Player;
import com.gah.draft.database.model.Selection;
import com.gah.draft.database.service.EntryService;
import com.gah.draft.database.service.PlayerService;
import com.gah.draft.database.service.SelectionService;
import com.gah.draft.utils.DraftUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SelectionService selectionService;

    @Autowired
    private EntryService entryService;

    @GetMapping("/upload-player-list")
    public String uploadPlayerList(){
        return "upload-player-list";
    }

    @PostMapping("/upload-player-csv")
    public String uploadPlayerCSV(@RequestParam("file")MultipartFile file, Model model){

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                CsvToBean<Player> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Player.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<Player> players = csvToBean.parse();

                for(Player player: players){
                    player.setPick(0);
                    player.setTeam("");
                    playerService.save(player);
                }

                List<Player> newPlayers = playerService.getAll();
                Collections.sort(newPlayers, Comparator.comparing(Player::getPlayerName));

                model.addAttribute("players", newPlayers);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        return "player-list";
    }

    @GetMapping("/player-list")
    public String playerList(Model model){
        List<Player> players = playerService.getAll();
        Collections.sort(players, Comparator.comparing(Player::getPlayerName));
        model.addAttribute("players", players);
        return "player-list";
    }

    @PostMapping("/update-selection-function")
    public String updateSelection(@RequestParam("name")String playerName, @RequestParam("pick")int pick, @RequestParam("team")String team){
        Player player = playerService.getPlayerByPlayerName(playerName);
        player.setTeam(team);
        player.setPick(pick);
        playerService.update(player);

        List<Selection> selectionsByName = selectionService.getAllByPlayerName(playerName);

        if (pick != 37){
        List<Entry> allEntries = entryService.getAll();

        for (Entry entry: allEntries){
            Selection selection = selectionService.getBySelectorAndPick(entry.getId(), player.getPick());
            if (!selection.getPlayerName().equals(player.getPlayerName())){

                if (player.getTeam().equals(selection.getTeam()) && !DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(player.getTeam())){
                    selection.setTradeCorrect(true);
                }
                    // If Giants does not equal Raiders AND Giants eqauls giants
                if (!DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(selection.getTeam()) && DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(player.getTeam())){
                    selection.setFailedTrade(true);
                }

                if (!DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(player.getTeam()) && !DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(selection.getTeam()) && selection.isTradeCorrect() == false){
                    selection.setTradeOccurred(true);
                }

                selectionByPickValidation(entry.getId(), player.getPick(), player);
            }
            selectionService.update(selection);
        }}

        for (Selection selection: selectionsByName){
            int score = selection.getPick() - player.getPick();
            selection.setScore(Math.abs(score));
            selection.setProjectedScore(Math.abs(score));
            if (pick != 37){
            if (score == 0){
                selection.setCorrect(true);

                if (player.getTeam().equals(selection.getTeam()) && !DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(player.getTeam())){
                    selection.setTradeAndTeamCorrect(true);
                }

                if (!DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(selection.getTeam()) && !DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(player.getTeam()) && selection.isTradeAndTeamCorrect() == false){
                    selection.setTradeCorrect(true);
                }

                if (!player.getTeam().equals(selection.getTeam()) && DraftUtils.DRAFT_ORDER.get(player.getPick()).equals(player.getTeam())){
                    selection.setFailedTrade(true);
                }

            } else {
                selectionByPickValidation(selection.getSelector(), player.getPick(), player);
            }}
            selectionService.update(selection);
        }
        if (pick == 37){
            List<Player> undrafteds = playerService.getAllBySelectedAndAndPick(true,0);

            for (Player undrafted: undrafteds){
                List<Selection> missedPicks = selectionService.getAllByPlayerName(undrafted.getPlayerName());
                for (Selection missedPick: missedPicks){
                    int score = 37 - missedPick.getPick();
                    missedPick.setScore(Math.abs(score));
                    missedPick.setProjectedScore(Math.abs(score));
                    selectionService.update(missedPick);
                }
                undrafted.setPick(pick);
                undrafted.setTeam(team);
                playerService.update(undrafted);
            }
        }
        return "success";
    }

    @GetMapping("/selected-players")
    public String selectedPlayers(Model model){
        List<Player> selectedPlayers = playerService.getAllBySelected(true);
        Collections.sort(selectedPlayers, Comparator.comparing(Player::getPlayerName));
        model.addAttribute("players", selectedPlayers);
        return "player-list";
    }

    @GetMapping("/update-selection")
    public String getFormForSelectionUpdate(Model model){
        model.addAttribute("teams", DraftUtils.TEAM_LIST);
        model.addAttribute("picks", DraftUtils.PICK_NUMBERS);
        model.addAttribute("players", getPlayerNamesList());
        return "update-selection";
    }

    @PostMapping("/fix-mistake")
    public String fixMistake(@RequestParam("name")String playerName){
        Player player = playerService.getPlayerByPlayerName(playerName);

        List<Selection> selectionsPickNumber = selectionService.getAllByPick(player.getPick());

        for (Selection selection: selectionsPickNumber){
            selection.setPositionCorrect(false);
            selection.setTradeOccurred(false);
            selection.setFailedTrade(false);
            selectionService.update(selection);
        }

        List<Selection> selections = selectionService.getAllByPlayerName(playerName);

        for (Selection selection: selections){
            selection.setTradeAndTeamCorrect(false);
            selection.setTradeCorrect(false);
            selection.setScore(0);
            selection.setProjectedScore(37 - selection.getPick());
            selection.setCorrect(false);
            selection.setFailedTrade(false);
            selectionService.update(selection);
        }
        player.setPick(0);
        player.setTeam("");
        playerService.update(player);

        return "success";
    }

    @GetMapping("/fix-selection")
    public String fixSelection(Model model){
        model.addAttribute("players", getPlayerNamesList());
        return "fix-selection";
    }

    @GetMapping("/draft-results")
    public String draftResults(Model model){
        List<Player> players = playerService.getAll();
        List<Player> draftedPlayers = new ArrayList<>();
        for (Player player: players){
            if (player.getPick() > 0){
                draftedPlayers.add(player);
            }
        }
        Collections.sort(draftedPlayers, Comparator.comparing(Player::getPick));
        model.addAttribute("players", draftedPlayers);
        return "drafted-players";
    }

    private void selectionByPickValidation(int selector, int pick, Player player){
        Selection selectionByPick = selectionService.getBySelectorAndPick(selector, pick);
        if (selectionByPick.getPosition().equals(player.getPosition())){
            selectionByPick.setPositionCorrect(true);
            selectionService.update(selectionByPick);
        }
    }

    public List<String> getPlayerNamesList(){
        List<Player> players = playerService.getAll();
        Collections.sort(players, Comparator.comparing(Player::getPlayerName));
        List<String> playerNames = new ArrayList<>();
        for (Player player: players){
            playerNames.add(player.getPlayerName());
        }
        return playerNames;
    }


}
