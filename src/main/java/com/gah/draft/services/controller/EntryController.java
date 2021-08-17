package com.gah.draft.services.controller;

import com.gah.draft.database.model.Entry;
import com.gah.draft.database.model.Player;
import com.gah.draft.database.model.Selection;
import com.gah.draft.database.service.EntryService;
import com.gah.draft.database.service.PlayerService;
import com.gah.draft.database.service.SelectionService;
import com.gah.draft.services.model.DisplayEntry;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class EntryController {
    @Autowired
    private EntryService entryService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SelectionService selectionService;

    @GetMapping("/")
    public String leaderBoard(Model model){
        List<Entry> entries = entryService.getAll();
        List<DisplayEntry> displayEntries = new ArrayList<>();

        for (Entry entry: entries){
            DisplayEntry displayEntry = new DisplayEntry();
            displayEntry.setName(entry.getName());
            List<Selection> selections = selectionService.getAllBySelector(entry.getId());
            int score = 0;
            int correct = 0;
            int tradeCorrect = 0;
            int tradeAndTeamCorrect = 0;
            int positionCorrect = 0;
            int failedTrade = 0;
            int tradeOccurred = 0;
            int projectedScore = 0;
            for (Selection selection: selections){
                projectedScore = projectedScore + selection.getProjectedScore();
                score += selection.getScore();

                if (selection.isCorrect()){
                    correct++;
                }
                if (selection.isTradeAndTeamCorrect()){
                    tradeAndTeamCorrect++;
                    score = score - 8;
                }
                if (selection.isTradeCorrect()){
                    tradeCorrect++;
                    score = score - 4;
                }
                if (selection.isPositionCorrect()){
                    positionCorrect++;
                    score = score - 1;
                }

                if (selection.isFailedTrade()){
                    failedTrade++;
                    score = score + 2;
                }

                if (selection.isTradeOccurred()){
                    tradeOccurred++;
                    score = score - 2;
                }
            }
            displayEntry.setScore(score);
            displayEntry.setCorrect(correct);
            displayEntry.setPositionCorrect(positionCorrect);
            displayEntry.setTradeCorrect(tradeCorrect);
            displayEntry.setTradeAndTeamCorrect(tradeAndTeamCorrect);
            displayEntry.setFailedTrade(failedTrade);
            displayEntry.setTradeOccurred(tradeOccurred);
            displayEntry.setProjectedScore(projectedScore);
            displayEntries.add(displayEntry);
        }

        Collections.sort(displayEntries, Comparator.comparing(DisplayEntry::getScore).thenComparing(DisplayEntry::getCorrect).thenComparing(DisplayEntry::getTradeAndTeamCorrect).thenComparing(DisplayEntry::getTradeCorrect).thenComparing(DisplayEntry::getPositionCorrect));

        int rank = 1;

        for (DisplayEntry displayEntry: displayEntries){
            displayEntry.setRank(rank++);
        }

        model.addAttribute("entries", displayEntries);

        return "index";
    }

    @GetMapping("/upload-entry")
    public String uploadPlayerList(){
        return "upload-entry";
    }

    @PostMapping("/upload-entry-csv")
    public String uploadEntryCSV(@RequestParam("file") MultipartFile file, @RequestParam("name")String name, Model model){
        Entry entry = new Entry();
        entry.setName(name);
        System.out.println(entry.toString());
        entryService.save(entry);

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                CsvToBean<Selection> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Selection.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<Selection> selections = csvToBean.parse();

                for(Selection selection: selections){
                    selection.setSelector(entry.getId());
                    selection.setScore(0);
                    selection.setProjectedScore(37 - selection.getPick());
                    String playerName = selection.getPlayerName();
                    Player player = playerService.getPlayerByPlayerName(playerName);
                    player.setSelected(true);
                    selection.setPosition(player.getPosition());
                    playerService.update(player);
                    selectionService.save(selection);
                }

                model.addAttribute("entry", entry);
                model.addAttribute("selections", selections);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        return "show-entry";
    }

    @GetMapping("/show-entry/{name}")
    public String showEntry(@PathVariable("name")String name, Model model){
        Entry entry = entryService.getByName(name);

        List<Selection> selections = selectionService.getAllBySelector(entry.getId());
        Collections.sort(selections, Comparator.comparing(Selection::getPick));

        model.addAttribute("entry", entry);
        model.addAttribute("selections", selections);
        return "show-entry";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/remove-entry")
    public String removeEntry(Model model){
        List<Entry> entries = entryService.getAll();
        List<String> entryNames = new ArrayList<>();
        for (Entry entry: entries){
            entryNames.add(entry.getName());
        }
        model.addAttribute("entries", entryNames);

        return ("remove-entry");
    }

    @PostMapping("/delete-entry")
    public String removeEntry(@RequestParam("name")String entryName){
        Entry entry = entryService.getByName(entryName);

        List<Selection> selections = selectionService.getAllBySelector(entry.getId());
        for (Selection selection: selections){
            selectionService.delete(selection);
        }
        entryService.delete(entry);

        return "success";
    }
}
