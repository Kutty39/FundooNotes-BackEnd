package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.NoteDto;
import com.blbz.fundooapi.dto.NoteStatusDto;
import com.blbz.fundooapi.dto.NotesStatusDto;
import com.blbz.fundooapi.entiry.NoteInfo;
import com.blbz.fundooapi.exception.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NoteService {
    int createNote(NoteDto noteDto, String jwtHeader) throws  InvalidUserException;

    int noteAction(NoteDto noteDto, String jwtHeader, boolean edit) throws  InvalidUserException;

    int editNote(NoteDto noteDto, String jwtHeader) throws  InvalidUserException;

    int deleteNote(int noteId) throws NoteNotFoundException;

    int deleteNotes(List<Integer> noteId) throws NoteNotFoundException;

    int updateStatus(NotesStatusDto noteStatusDto, String jwtHeader) throws InvalidNoteStatus, InvalidUserException;

    int updateStatus(NoteStatusDto noteStatusDto, String jwtHeader) throws  InvalidUserException, NoteNotFoundException, InvalidNoteStatus;

    List<NoteInfo> getNotesByLabel(String labelText, String jwtHeader) throws LabelNotFoundException,  InvalidUserException;

    List<NoteInfo> getAllNotes(String jwtHeader) throws  InvalidUserException, NoteNotFoundException;

    NoteInfo getNotes(int id, String jwtHeader) throws  InvalidUserException, NoteNotFoundException;

    List<NoteInfo> getNotesByStatus(String statusText, String jwtHeader) throws  InvalidUserException, NoteStatusNotFoundException;
}
