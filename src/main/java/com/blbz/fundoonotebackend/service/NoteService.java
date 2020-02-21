package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.dto.NoteDto;
import com.blbz.fundoonotebackend.dto.NoteStatusDto;
import com.blbz.fundoonotebackend.dto.NotesStatusDto;
import com.blbz.fundoonotebackend.exception.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NoteService {
    NoteDto createNote(NoteDto noteDto, String jwtHeader) throws  InvalidUserException;

    NoteDto noteAction(NoteDto noteDto, String jwtHeader, boolean edit) throws  InvalidUserException;

    NoteDto editNote(NoteDto noteDto, String jwtHeader) throws  InvalidUserException;

    int deleteNote(int noteId,String jwtHeader) throws NoteNotFoundException, InvalidUserException;

    int deleteNotes(List<Integer> noteId, String jwtHeader) throws NoteNotFoundException, InvalidUserException;

    int updateStatus(NotesStatusDto noteStatusDto, String jwtHeader) throws InvalidNoteStatus, InvalidUserException;

    int updateStatus(NoteStatusDto noteStatusDto, String jwtHeader) throws  InvalidUserException, NoteNotFoundException, InvalidNoteStatus;

    List<NoteDto> getNotesByLabel(String labelText, String jwtHeader) throws LabelNotFoundException,  InvalidUserException;

    List<NoteDto> getAllNotes(String jwtHeader) throws  InvalidUserException, NoteNotFoundException;

    NoteDto getNotes(int id, String jwtHeader) throws  InvalidUserException, NoteNotFoundException;

    List<NoteDto> getNotesByStatus(String statusText, String jwtHeader) throws  InvalidUserException, NoteStatusNotFoundException;

    List<NoteDto> getNotesByRemainder(String header) throws InvalidUserException;

    List<NoteDto> searchAll(String text, String header) throws InvalidUserException;
}
