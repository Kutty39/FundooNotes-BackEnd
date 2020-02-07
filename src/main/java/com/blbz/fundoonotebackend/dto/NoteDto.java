package com.blbz.fundoonotebackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@Component
@Scope(value ="prototype")
public class NoteDto {
    private int noteId;
    private String noteTitle;
    private String noteText;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss a")
    private Date noteRemainder;
    private String noteRemainderLocation;
    private boolean showTick = false;
    private boolean isPinned = false;
    private String colour;
    private String noteStatus;
    private List<String> collaborator;
    private List<String> labels;
}
