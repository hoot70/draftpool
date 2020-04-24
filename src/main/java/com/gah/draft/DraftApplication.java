package com.gah.draft;

import com.gah.draft.utils.DraftUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DraftApplication {

    public static void main(String[] args) {
        SpringApplication.run(DraftApplication.class, args);
        DraftUtils.loadDraftOrder();
        DraftUtils.loadTeamList();
        DraftUtils.loadPickList();
    }

}
