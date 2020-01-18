package com.blbz.fundoonotebackend.serviceimpl;

import com.blbz.fundoonotebackend.entiry.NoteStatus;
import com.blbz.fundoonotebackend.repository.NoteStatusRepo;
import com.blbz.fundoonotebackend.service.NoteStatusService;
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
