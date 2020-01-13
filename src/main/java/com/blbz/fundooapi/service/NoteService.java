package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.NoteDto;
import com.blbz.fundooapi.dto.NoteStatusDto;
import com.blbz.fundooapi.dto.NotesStatusDto;
import com.blbz.fundooapi.entiry.NoteInfo;
import com.blbz.fundooapi.exception.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public interface NoteService {
    int createNote(NoteDto noteDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException;

    int noteAction(NoteDto noteDto, HttpServletRequest httpServletRequest, boolean edit) throws HeaderMissingException, InvalidUserException;

    int editNote(NoteDto noteDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException;

    int deleteNote(int noteId) throws NoteNotFoundException;

    int deleteNotes(List<Integer> noteId) throws NoteNotFoundException;

    int updateStatus(NotesStatusDto noteStatusDto, HttpServletRequest httpServletRequest) throws InvalidNoteStatus, InvalidUserException, HeaderMissingException;

    int updateStatus(NoteStatusDto noteStatusDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteNotFoundException, InvalidNoteStatus;

    List<NoteInfo> getNotesByLabel(String labelText, HttpServletRequest httpServletRequest) throws LabelNotFoundException, HeaderMissingException, InvalidUserException;

    List<NoteInfo> getAllNotes(HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteNotFoundException;

    NoteInfo getNotes(int id, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteNotFoundException;

    List<NoteInfo> getNotesByStatus(String statusText, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteStatusNotFoundException;
}
