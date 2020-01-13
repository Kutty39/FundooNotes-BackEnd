package com.blbz.fundooapi.serviceimpl;

import com.blbz.fundooapi.entiry.NoteStatus;
import com.blbz.fundooapi.repository.NoteStatusRepo;
import com.blbz.fundooapi.service.NoteStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteStatusServiceImpl implements NoteStatusService {
    private final NoteStatusRepo noteStatusRepo;

    @Autowired
    public NoteStatusServiceImpl(NoteStatusRepo noteStatusRepo) {
        this.noteStatusRepo = noteStatusRepo;
    }

    @Override
    public NoteStatus getStatus(String status) {
        return noteStatusRepo.findByStatusText(status);
    }
}
