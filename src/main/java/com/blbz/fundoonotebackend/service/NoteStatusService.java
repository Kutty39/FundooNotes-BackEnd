package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.entiry.NoteStatus;
import org.springframework.stereotype.Component;

@Component
public interface NoteStatusService {
    NoteStatus getStatus(String status);
}
