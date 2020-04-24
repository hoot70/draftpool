package com.gah.draft.utils;

import java.util.*;

public class DraftUtils {
    public static final HashMap<Integer, String> DRAFT_ORDER = new HashMap<>();
    public static final List<String> TEAM_LIST = new ArrayList<>();
    public static final List<Integer> PICK_NUMBERS = new ArrayList<>();


    public static void loadDraftOrder(){
        DRAFT_ORDER.put(1, "Bengals");
        DRAFT_ORDER.put(2, "Redskins");
        DRAFT_ORDER.put(3, "Lions");
        DRAFT_ORDER.put(4, "Giants");
        DRAFT_ORDER.put(5, "Dolphins");
        DRAFT_ORDER.put(6, "Chargers");
        DRAFT_ORDER.put(7, "Panthers");
        DRAFT_ORDER.put(8, "Cardinals");
        DRAFT_ORDER.put(9, "Jaguars");
        DRAFT_ORDER.put(10, "Browns");
        DRAFT_ORDER.put(11, "Jets");
        DRAFT_ORDER.put(12, "Raiders");
        DRAFT_ORDER.put(13, "49ers");
        DRAFT_ORDER.put(14, "Buccaneers");
        DRAFT_ORDER.put(15, "Broncos");
        DRAFT_ORDER.put(16, "Falcons");
        DRAFT_ORDER.put(17, "Cowboys");
        DRAFT_ORDER.put(18, "Dolphins");
        DRAFT_ORDER.put(19, "Raiders");
        DRAFT_ORDER.put(20, "Jaguars");
        DRAFT_ORDER.put(21, "Eagles");
        DRAFT_ORDER.put(22, "Vikings");
        DRAFT_ORDER.put(23, "Patriots");
        DRAFT_ORDER.put(24, "Saints");
        DRAFT_ORDER.put(25, "Vikings");
        DRAFT_ORDER.put(26, "Dolphins");
        DRAFT_ORDER.put(27, "Seahawks");
        DRAFT_ORDER.put(28, "Ravens");
        DRAFT_ORDER.put(29, "Titans");
        DRAFT_ORDER.put(30, "Packers");
        DRAFT_ORDER.put(31, "49ers");
        DRAFT_ORDER.put(32, "Chiefs");
    }

    public static void loadTeamList(){
        TEAM_LIST.add("Cardinals");
        TEAM_LIST.add("Falcons");
        TEAM_LIST.add("Ravens");
        TEAM_LIST.add("Bills");
        TEAM_LIST.add("Panthers");
        TEAM_LIST.add("Bears");
        TEAM_LIST.add("Bengals");
        TEAM_LIST.add("Browns");
        TEAM_LIST.add("Cowboys");
        TEAM_LIST.add("Broncos");
        TEAM_LIST.add("Lions");
        TEAM_LIST.add("Packers");
        TEAM_LIST.add("Texans");
        TEAM_LIST.add("Colts");
        TEAM_LIST.add("Jaguars");
        TEAM_LIST.add("Chiefs");
        TEAM_LIST.add("Chargers");
        TEAM_LIST.add("Rams");
        TEAM_LIST.add("Dolphins");
        TEAM_LIST.add("Vikings");
        TEAM_LIST.add("Patriots");
        TEAM_LIST.add("Saints");
        TEAM_LIST.add("Giants");
        TEAM_LIST.add("Jets");
        TEAM_LIST.add("Raiders");
        TEAM_LIST.add("Eagles");
        TEAM_LIST.add("Steelers");
        TEAM_LIST.add("49ers");
        TEAM_LIST.add("Seahawks");
        TEAM_LIST.add("Buccaneers");
        TEAM_LIST.add("Titans");
        TEAM_LIST.add("Redskins");

        Collections.sort(TEAM_LIST);
        TEAM_LIST.add("Undrafted");
    }

    public static void loadPickList(){
        Set<Integer> pickSet = DRAFT_ORDER.keySet();

        for(Integer pick: pickSet){
            PICK_NUMBERS.add(pick);
        }

        PICK_NUMBERS.add(37);
    }
}
