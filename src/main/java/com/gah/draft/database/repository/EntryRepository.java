package com.gah.draft.database.repository;

import com.gah.draft.database.model.Entry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends CrudRepository<Entry, Integer> {
    Entry findByName(String name);

    List<Entry> findAll();;
}
