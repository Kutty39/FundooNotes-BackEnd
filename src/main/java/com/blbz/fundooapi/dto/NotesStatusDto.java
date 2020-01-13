package com.blbz.fundooapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotesStatusDto {
    private List<Integer> noteId;
    private String status;
}
