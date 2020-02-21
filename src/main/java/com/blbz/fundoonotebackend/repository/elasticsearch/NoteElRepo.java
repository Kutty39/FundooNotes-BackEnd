package com.blbz.fundoonotebackend.repository.elasticsearch;

import com.blbz.fundoonotebackend.entiry.NoteInfoEl;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NoteElRepo extends ElasticsearchCrudRepository<NoteInfoEl, Integer> {
    @Query("{\"bool\":{\"must\":[{\"match\":{\"collaborator.eid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    List<NoteInfoEl> findByCollaborator(String email);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"collaborator.eid\":\"?0\"}},{\"term\":{\"noteId\":\"?1\"}}],\"must_not\":[],\"should\":[]}}")
    NoteInfoEl findByCollaboratorAndNoteId(String eid, int id);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"collaborator.eid\":\"?1\"}},{\"match\":{\"labels.labelText\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    List<NoteInfoEl> findByLabelsAndCollaborator(String labelText, String eid);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"collaborator.eid\":\"?1\"}},{\"match\":{\"noteStatus.statusText\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    List<NoteInfoEl> findByNoteStatusAndCollaborator(String statusText, String eid);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"collaborator.eid\":\"?0\"}},{\"wildcard\":{\"noteRemainder\":\"*\"}}],\"must_not\":[],\"should\":[]}}")
    List<NoteInfoEl> findByCollaboratorAndNoteRemainderNotNull(String eid);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"collaborator.eid\":\"?1\"}},{\"query_string\":{\"query\":\"*?0*\"}}],\"must_not\":[],\"should\":[]}}")
    List<NoteInfoEl> findAny(String text,String email);
}
