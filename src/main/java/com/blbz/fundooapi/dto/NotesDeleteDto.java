package com.blbz.fundooapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotesDeleteDto {
    private List<Integer> noteId;
}
