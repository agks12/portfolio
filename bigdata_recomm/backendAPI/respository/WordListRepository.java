package com.example.pppp.Bookfolder.respository;

import com.example.pppp.Bookfolder.entity.WordList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordListRepository extends JpaRepository<WordList, Integer> {
}
