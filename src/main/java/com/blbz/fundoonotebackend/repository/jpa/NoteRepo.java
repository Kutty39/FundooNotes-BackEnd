package com.blbz.fundoonotebackend.repository.jpa;

import com.blbz.fundoonotebackend.entiry.NoteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NoteRepo extends JpaRepository<NoteInfo,Integer> {
    @Query("from NoteInfo where noteId=:id")
    NoteInfo findByUniqKey(int id);
}
