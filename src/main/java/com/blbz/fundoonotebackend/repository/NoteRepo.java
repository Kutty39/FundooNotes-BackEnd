package com.blbz.fundoonotebackend.repository;

import com.blbz.fundoonotebackend.entiry.Label;
import com.blbz.fundoonotebackend.entiry.NoteInfo;
import com.blbz.fundoonotebackend.entiry.NoteStatus;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NoteRepo extends JpaRepository<NoteInfo,Integer> {
    @Query("from NoteInfo where noteId=:id")
    NoteInfo findByUniqKey(int id);
    List<NoteInfo> findByCollaborator(UserInfo collaborator);
    NoteInfo findByCollaboratorAndNoteId(UserInfo userInfo,int id);
    List<NoteInfo> findByLabelsAndCollaborator(Label label, UserInfo userInfo);
    List<NoteInfo> findByNoteStatusAndCollaborator(NoteStatus noteStatus,UserInfo userInfo);
    List<NoteInfo> findByCollaboratorAndNoteRemainderNotNull(UserInfo userInfo);
}
