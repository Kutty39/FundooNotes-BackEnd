package com.blbz.fundooapi.repository;

import com.blbz.fundooapi.entiry.Label;
import com.blbz.fundooapi.entiry.NoteInfo;
import com.blbz.fundooapi.entiry.NoteStatus;
import com.blbz.fundooapi.entiry.UserInfo;
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
    List<NoteInfo> findByCreatedBy(UserInfo collaborator);
    NoteInfo findByCollaboratorAndNoteId(UserInfo userInfo,int id);
    List<NoteInfo> findByLabelsAndAndCollaborator(Label label,UserInfo userInfo);
    List<NoteInfo> findByNoteStatusAndCollaborator(NoteStatus noteStatus,UserInfo userInfo);
}
