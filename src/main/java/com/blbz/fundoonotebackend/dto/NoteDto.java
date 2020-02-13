package com.blbz.fundoonotebackend.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class NoteDto {
    private int noteId;
    private String noteTitle;
    private String noteText;
    private String noteRemainder;
    private String noteRemainderLocation;
    private boolean showTick = false;
    private boolean isPinned = false;
    private String colour;
    private String noteStatus;
    private List<String> collaborator;
    private List<String> labels;
}
