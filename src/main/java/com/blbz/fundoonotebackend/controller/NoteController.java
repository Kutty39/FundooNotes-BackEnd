package com.blbz.fundoonotebackend.controller;

import com.blbz.fundoonotebackend.dto.NoteDto;
import com.blbz.fundoonotebackend.dto.NoteStatusDto;
import com.blbz.fundoonotebackend.dto.NotesDeleteDto;
import com.blbz.fundoonotebackend.dto.NotesStatusDto;
import com.blbz.fundoonotebackend.exception.*;
import com.blbz.fundoonotebackend.responce.GeneralResponse;
import com.blbz.fundoonotebackend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getNoteByLabel(@PathVariable String label, @RequestHeader("Authorization") String header) throws  InvalidUserException, LabelNotFoundException, ParameterEmptyException {
        if(label==null || label.isEmpty()){
            throw new ParameterEmptyException("label text not passed");
        }
        generalResponse.setResponse(noteService.getNotesByLabel(label,header.replace("Bearer ","")));
        return ResponseEntity.ok(generalResponse);
    }

    @GetMapping("/notes/status/{status}")
    public ResponseEntity<?> getNoteByStatus(@PathVariable String status, @RequestHeader("Authorization") String header) throws  InvalidUserException,  NoteStatusNotFoundException, ParameterEmptyException {
        if(status==null || status.isEmpty()){
            throw new ParameterEmptyException("status text not passed");
        }
        generalResponse.setResponse(noteService.getNotesByStatus(status,header.replace("Bearer ","")));
        return ResponseEntity.ok(generalResponse);
    }


    @GetMapping("/notes")
    public ResponseEntity<?> getNote( @RequestHeader("Authorization") String header) throws NoteNotFoundException, InvalidUserException {

        generalResponse.setResponse(noteService.getAllNotes(header.replace("Bearer ","")));
        return ResponseEntity.ok(generalResponse);
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNote( @RequestHeader("Authorization") String header, @PathVariable String id) throws NoteNotFoundException, InvalidUserException, ParameterEmptyException {
        if(id==null || id.isEmpty()){
            throw new ParameterEmptyException("id not passed");
        }
        generalResponse.setResponse(noteService.getNotes(Integer.parseInt(id), header.replace("Bearer ","")));
        return ResponseEntity.ok(generalResponse);
    }

    @PostMapping("/notes")
    public ResponseEntity<?> createNote(@RequestBody NoteDto noteDto, @RequestHeader("Authorization") String header) throws InvalidUserException {
        if (noteDto.getNoteText() != null || noteDto.getNoteTitle() != null ||
                noteDto.getNoteRemainder() != null || noteDto.getCollaborator() != null) {
            int noteId = noteService.createNote(noteDto, header.replace("Bearer ",""));
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
    public ResponseEntity<?> editNotes(@RequestBody NoteDto noteDto, @RequestHeader("Authorization") String header) throws InvalidUserException {
        int noteId = noteService.editNote(noteDto, header.replace("Bearer ",""));
        if (noteId > 0) {
            generalResponse.setResponse(noteId);
            return ResponseEntity.ok().body(generalResponse);
        } else {
            generalResponse.setResponse("Something went wrong");
            return ResponseEntity.badRequest().body(generalResponse);
        }
    }

    @PutMapping("/note/status")
    public ResponseEntity<?> updatedStatus(@RequestBody NoteStatusDto noteStatusDto, @RequestHeader("Authorization") String header) throws InvalidUserException, InvalidNoteStatus, NoteNotFoundException {
        int noteID = noteService.updateStatus(noteStatusDto, header.replace("Bearer ",""));
        generalResponse.setResponse(noteID);
        if (noteID > 0) {
            return ResponseEntity.ok(generalResponse);
        } else {
            return ResponseEntity.badRequest().body(generalResponse);
        }
    }

    @PutMapping("/notes/status")
    public ResponseEntity<?> updatedStatus(@RequestBody NotesStatusDto notesStatusDto, @RequestHeader("Authorization") String header) throws InvalidUserException, InvalidNoteStatus {
        int noteId = noteService.updateStatus(notesStatusDto, header.replace("Bearer ",""));
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
