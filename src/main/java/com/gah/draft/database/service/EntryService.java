package com.gah.draft.database.service;

import com.gah.draft.database.model.Entry;
import com.gah.draft.database.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryService {
    @Autowired
    private EntryRepository entryRepository;

    public Entry getByName(String name){
        return entryRepository.findByName(name);
    }

    public List<Entry> getAll(){
        return entryRepository.findAll();
    }

    public void save(Entry entry){
        entryRepository.save(entry);
    }

    public void delete(Entry entry){
        entryRepository.delete(entry);
    }
}
