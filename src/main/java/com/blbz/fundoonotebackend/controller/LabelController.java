package com.blbz.fundoonotebackend.controller;

import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.LabelNotFoundException;
import com.blbz.fundoonotebackend.exception.ParameterEmptyException;
import com.blbz.fundoonotebackend.responce.GeneralResponse;
import com.blbz.fundoonotebackend.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    private final LabelService labelService;
    private final GeneralResponse generalResponse;

    @Autowired
    public LabelController(LabelService labelService, GeneralResponse generalResponse) {
        this.labelService = labelService;
        this.generalResponse = generalResponse;
    }

    @GetMapping
    public ResponseEntity<?> getAllLabels(@RequestHeader("Authorization") String header) throws  InvalidUserException {
        generalResponse.setResponse(labelService.getAllLabels(header));
        return ResponseEntity.ok(generalResponse);
    }

    @GetMapping("/{label}")
    public ResponseEntity<?> getLabel(@PathVariable String label, @RequestHeader("Authorization") String header) throws LabelNotFoundException,  InvalidUserException, ParameterEmptyException {
        if(label==null || label.isEmpty()){
            throw new ParameterEmptyException("label text not passed");
        }
        generalResponse.setResponse(labelService.getLabel(label,header));
        return ResponseEntity.ok(generalResponse);
    }

    @PostMapping
    public ResponseEntity<?> createLabel(@RequestParam String labelText, @RequestHeader("Authorization") String header) throws InvalidUserException {
        generalResponse.setResponse(labelService.createLabel(labelText,header));
        return ResponseEntity.ok(generalResponse);
    }
    @PutMapping
    public ResponseEntity<?> editLabel(String oldLabel,String newLabel, @RequestHeader("Authorization") String header) throws LabelNotFoundException, InvalidUserException {
        generalResponse.setResponse(labelService.editLabel(oldLabel,newLabel,header));
        return ResponseEntity.ok(generalResponse);
    }
    @DeleteMapping("/{labelText}")
    public ResponseEntity<?> deleteLabel(@PathVariable String labelText,@RequestHeader("Authorization") String header) throws InvalidUserException {
        labelService.deleteLabel(labelText,header);
        generalResponse.setResponse("Label Deleted");
        return ResponseEntity.ok(generalResponse);
    }
}
