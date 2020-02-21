package com.blbz.fundoonotebackend.entiry;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@Component
@Document(indexName = "webappdb",type = "noteinfo")
public class NoteInfoEl {
    @Id
    private int noteId;
    private String noteTitle;
    private String noteText;
    private String noteRemainder;
    private String noteRemainderLocation;
    private boolean showTick = false;
    private Date noteCreatedOn;
    private Date noteLastEditedOn;
    private boolean pinned=false;
    private Colors colors;
    private NoteStatus noteStatus;
    private UserInfo createdBy;
    private UserInfo editedBy;
    private List<UserInfo> collaborator;
    private List<Label> labels;

}
