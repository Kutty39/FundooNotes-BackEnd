package com.blbz.fundoonotebackend.repository;

import com.blbz.fundoonotebackend.entiry.NoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NoteStatusRepo extends JpaRepository<NoteStatus,Integer> {
    NoteStatus findByStatusText(String status);
}
