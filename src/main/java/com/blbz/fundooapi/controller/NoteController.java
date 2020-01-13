package com.blbz.fundooapi.controller;

import com.blbz.fundooapi.dto.NoteDto;
import com.blbz.fundooapi.dto.NoteStatusDto;
import com.blbz.fundooapi.dto.NotesDeleteDto;
import com.blbz.fundooapi.dto.NotesStatusDto;
import com.blbz.fundooapi.exception.*;
import com.blbz.fundooapi.responce.GeneralResponse;
import com.blbz.fundooapi.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class NoteController {
    private final NoteService noteService;
    private GeneralResponse generalResponse;

    @Autowired
    public NoteController(GeneralResponse generalResponse, NoteService noteService) {
        this.generalResponse = generalResponse;
        this.noteService = noteService;
    }

    @GetMapping("/notes/label/{label}")
    public ResponseEntity<?> getNoteByLabel(@PathVariable String label, HttpServletRequest httpServletRequest) throws NoteNotFoundException, HeaderMissingException, InvalidUserException, LabelNotFoundException, ParameterEmptyException {
        if(label==null || label.isEmpty()){
            throw new ParameterEmptyException("label text not passed");
        }
        generalResponse.setResponse(noteService.getNotesByLabel(label,httpServletRequest));
        return ResponseEntity.ok(generalResponse);
    }

    @GetMapping("/notes/status/{status}")
    public ResponseEntity<?> getNoteByStatus(@PathVariable String status, HttpServletRequest httpServletRequest) throws NoteNotFoundException, HeaderMissingException, InvalidUserException, LabelNotFoundException, NoteStatusNotFoundException, ParameterEmptyException {
        if(status==null || status.isEmpty()){
            throw new ParameterEmptyException("status text not passed");
        }
        generalResponse.setResponse(noteService.getNotesByStatus(status,httpServletRequest));
        return ResponseEntity.ok(generalResponse);
    }


    @GetMapping("/notes")
    public ResponseEntity<?> getNote(HttpServletRequest httpServletRequest) throws NoteNotFoundException, HeaderMissingException, InvalidUserException {

        generalResponse.setResponse(noteService.getAllNotes(httpServletRequest));
        return ResponseEntity.ok(generalResponse);
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNote(HttpServletRequest httpServletRequest, @PathVariable String id) throws NoteNotFoundException, HeaderMissingException, InvalidUserException, ParameterEmptyException {
        if(id==null || id.isEmpty()){
            throw new ParameterEmptyException("id not passed");
        }
        generalResponse.setResponse(noteService.getNotes(Integer.parseInt(id), httpServletRequest));
        return ResponseEntity.ok(generalResponse);
    }

    @PostMapping("/notes")
    public ResponseEntity<?> createNote(@RequestBody NoteDto noteDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException {
        if (noteDto.getNoteText() != null || noteDto.getNoteTitle() != null ||
                noteDto.getNoteRemainder() != null || noteDto.getCollaborator() != null) {
            int noteId = noteService.createNote(noteDto, httpServletRequest);
            generalResponse.setResponse(noteId);
            if (noteId > 0) {
                return ResponseEntity.ok(generalResponse);
            } else {
                return ResponseEntity.badRequest().body(generalResponse);
            }
        } else {
            generalResponse.setResponse("Any one of the fields is mandatory." +
                    "Title,Note Text, Remainder,Collaborator or Label");
            return ResponseEntity.badRequest().body(generalResponse);
        }
    }

    @PutMapping("/notes")
    public ResponseEntity<?> editNotes(@RequestBody NoteDto noteDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException {
        int noteId = noteService.editNote(noteDto, httpServletRequest);
        if (noteId > 0) {
            generalResponse.setResponse(noteId);
            return ResponseEntity.ok().body(generalResponse);
        } else {
            generalResponse.setResponse("Something went wrong");
            return ResponseEntity.badRequest().body(generalResponse);
        }
    }

    @PutMapping("/note/status")
    public ResponseEntity<?> updatedStatus(@RequestBody NoteStatusDto noteStatusDto, HttpServletRequest httpServletRequest) throws InvalidUserException, HeaderMissingException, InvalidNoteStatus, NoteNotFoundException {
        int noteID = noteService.updateStatus(noteStatusDto, httpServletRequest);
        generalResponse.setResponse(noteID);
        if (noteID > 0) {
            return ResponseEntity.ok(generalResponse);
        } else {
            return ResponseEntity.badRequest().body(generalResponse);
        }
    }

    @PutMapping("/notes/status")
    public ResponseEntity<?> updatedStatus(@RequestBody NotesStatusDto notesStatusDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, InvalidNoteStatus {
        int noteId = noteService.updateStatus(notesStatusDto, httpServletRequest);
        generalResponse.setResponse(noteId);
        if (noteId > 0) {
            return ResponseEntity.ok(generalResponse);
        } else {
            return ResponseEntity.badRequest().body(generalResponse);
        }
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable int id) throws NoteNotFoundException, ParameterEmptyException {
        if(id==0){
            throw new ParameterEmptyException("ID not passed");
        }
        int noteId = noteService.deleteNote(id);
        generalResponse.setResponse(noteId);
        return ResponseEntity.badRequest().body(generalResponse);
    }

    @DeleteMapping("/notes")
    public ResponseEntity<?> deleteNote(@RequestBody NotesDeleteDto notesDeleteDto) throws NoteNotFoundException {
        int noteID = noteService.deleteNotes(notesDeleteDto.getNoteId());
        generalResponse.setResponse(noteID);
        return ResponseEntity.ok(generalResponse);
    }
}
