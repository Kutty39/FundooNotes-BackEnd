package com.blbz.fundooapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class NoteDeleteDto {
    @NotNull(message = "Note ID needed")
    @Pattern(regexp = "[\\d]+",message = "ID should be numbers")
    private int noteId;
}
