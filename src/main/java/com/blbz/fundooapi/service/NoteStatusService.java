package com.blbz.fundooapi.service;

import com.blbz.fundooapi.entiry.NoteStatus;
import org.springframework.stereotype.Component;

@Component
public interface NoteStatusService {
    NoteStatus getStatus(String status);
}
