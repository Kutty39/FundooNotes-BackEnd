package com.blbz.fundooapi.serviceimpl;

import com.blbz.fundooapi.dto.LabelDto;
import com.blbz.fundooapi.dto.NoteDto;
import com.blbz.fundooapi.dto.NoteStatusDto;
import com.blbz.fundooapi.dto.NotesStatusDto;
import com.blbz.fundooapi.entiry.*;
import com.blbz.fundooapi.exception.*;
import com.blbz.fundooapi.repository.*;
import com.blbz.fundooapi.service.CustomMapper;
import com.blbz.fundooapi.service.JwtUtil;
import com.blbz.fundooapi.service.LabelService;
import com.blbz.fundooapi.service.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private final CustomMapper customMapper;
    private final NoteRepo noteRepo;
    private final LabelRepo labelRepo;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final NoteStatusRepo noteStatusRepo;
    private final ColorRepo colorRepo;
    private final LabelService labelService;
    private final LabelDto labelDto;
    private NoteInfo noteInfo;


    @Autowired
    public NoteServiceImpl(CustomMapper customMapper, NoteRepo noteRepo, NoteInfo noteInfo
            , LabelRepo labelRepo, UserRepo userRepo, JwtUtil jwtUtil, NoteStatusRepo noteStatusRepo,
                           ColorRepo colorRepo, LabelService labelService, LabelDto labelDto) {
        this.customMapper = customMapper;
        this.noteRepo = noteRepo;
        this.noteInfo = noteInfo;
        this.labelRepo = labelRepo;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.noteStatusRepo = noteStatusRepo;
        this.colorRepo = colorRepo;
        this.labelService = labelService;
        this.labelDto = labelDto;
    }

    @Override
    public int createNote(NoteDto noteDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException {
        return noteAction(noteDto, httpServletRequest, false);
    }

    @Override
    public int editNote(NoteDto noteDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException {
        return noteRepo.findByUniqKey(noteDto.getNoteId()) == null ? 0 : noteAction(noteDto, httpServletRequest, true);
    }

    @Override
    public int deleteNote(int noteId) throws NoteNotFoundException {
        if (noteRepo.findById(noteId).isPresent()) {
            noteRepo.deleteById(noteId);
            return 1;
        } else {
            throw new NoteNotFoundException();
        }
    }

    @Override
    public int deleteNotes(List<Integer> noteId) throws NoteNotFoundException {
        List<NoteInfo> noteInfos = noteRepo.findAllById(noteId);
        noteRepo.deleteAll(noteInfos);
        if (noteInfos.size() == 0) {
            throw new NoteNotFoundException();
        }
        return noteInfos.size();
    }

    @Override
    public int updateStatus(NotesStatusDto notesStatusDto, HttpServletRequest httpServletRequest) throws InvalidUserException, HeaderMissingException, InvalidNoteStatus {

        UserInfo userInfo = jwtUtil.validateHeader(httpServletRequest);
        List<NoteInfo> noteInfos = noteRepo.findAllById(notesStatusDto.getNoteId());
        NoteStatus noteStatus = noteStatusRepo.findByStatusText(notesStatusDto.getStatus());
        if (noteStatus != null) {
            List<NoteInfo> noteInfos1 = new ArrayList<>();
            for (NoteInfo info : noteInfos) {
                info.setNoteStatus(noteStatus);
                info.setEditedBy(userInfo);
                info.setNoteLastEditedOn(Date.from(Instant.now()));
                noteInfos1.add(info);
            }
            noteRepo.saveAll(noteInfos1);
            return noteInfos.size();
        }
        throw new InvalidNoteStatus();
    }

    @Override
    public int updateStatus(NoteStatusDto noteStatusDto, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteNotFoundException, InvalidNoteStatus {

        UserInfo userInfo = jwtUtil.validateHeader(httpServletRequest);
        noteInfo = noteRepo.findByUniqKey(noteStatusDto.getNoteId());
        if (noteInfo != null) {
            NoteStatus noteStatus = noteStatusRepo.findByStatusText(noteStatusDto.getStatus());
            if (noteStatus != null) {
                noteInfo.setNoteStatus(noteStatus);
                noteInfo.setEditedBy(userInfo);
                noteInfo.setNoteLastEditedOn(Date.from(Instant.now()));
                noteRepo.save(noteInfo);
                return noteInfo.getNoteId();
            } else {
                throw new InvalidNoteStatus();
            }
        } else {
            throw new NoteNotFoundException();
        }
    }

    @Override
    public List<NoteInfo> getNotesByLabel(String labelText, HttpServletRequest httpServletRequest) throws LabelNotFoundException, HeaderMissingException, InvalidUserException {
        UserInfo userInfo = jwtUtil.validateHeader(httpServletRequest);
        Label label = labelRepo.findByUniqKey(labelText);
        if (label != null) {
            return noteRepo.findByLabelsAndAndCollaborator(label, userInfo);
        }
        throw new LabelNotFoundException("\"" + labelText + "\" not found");
    }

    @Override
    @Transactional
    public List<NoteInfo> getAllNotes(HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(httpServletRequest);
        List<NoteInfo> noteInfos = noteRepo.findByCreatedBy(userInfo);
        if (noteInfos == null || noteInfos.get(0) == null) {
            throw new NoteNotFoundException();
        }
        return noteInfos;

    }

    @Override
    public NoteInfo getNotes(int id, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(httpServletRequest);
        NoteInfo noteInfo = noteRepo.findByCollaboratorAndNoteId(userInfo, id);
        if (noteInfo == null) {
            throw new NoteNotFoundException();
        }
        return noteInfo;
    }

    @Override
    public List<NoteInfo> getNotesByStatus(String statusText, HttpServletRequest httpServletRequest) throws HeaderMissingException, InvalidUserException, NoteStatusNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(httpServletRequest);
        NoteStatus noteStatus = noteStatusRepo.findByStatusText(statusText);
        if (noteStatus != null) {
            return noteRepo.findByNoteStatusAndCollaborator(noteStatus, userInfo);
        }
        throw new NoteStatusNotFoundException("\""+statusText+"\" not found");
    }

    @Override
    public int noteAction(NoteDto noteDto, HttpServletRequest httpServletRequest, boolean edit) throws HeaderMissingException, InvalidUserException {
        UserInfo createdBy = jwtUtil.validateHeader(httpServletRequest);
        String userEmail = createdBy.getEid();
        BeanUtils.copyProperties(noteDto, noteInfo);
        if (edit) {
            NoteInfo noteInfo1 = noteRepo.findByUniqKey(noteDto.getNoteId());
            noteInfo.setCreatedBy(noteInfo1.getCreatedBy());
            noteInfo.setNoteCreatedOn(noteInfo1.getNoteCreatedOn());
        } else {
            BeanUtils.copyProperties(noteDto, noteInfo);
        }
        noteInfo.setNoteText(noteDto.getNoteText());

        noteInfo.setNoteTitle(noteDto.getNoteTitle());

        List<Label> labelList = noteDto.getLabels() != null ? customMapper.getListVal(noteDto.getLabels(), Label.class, labelRepo) : null;
        if (labelList != null) {
            for (int i = 0; i < labelList.size(); i++) {
                if (labelList.get(i) == null) {
                    labelDto.setLabelText(noteDto.getLabels().get(i));
                    labelList.remove(i);
                    labelList.add(i, labelService.createLabel(labelDto));
                }
                ++i;
            }
        }
        noteInfo.setLabels(labelList);
        List<UserInfo> userInfos = noteDto.getCollaborator() != null ? customMapper.getListVal(noteDto.getCollaborator(), UserInfo.class, userRepo) : new ArrayList<>();
        if (noteDto.getCollaborator() == null) {
            userInfos.add(createdBy);
        } else if (noteDto.getCollaborator().get(0) == null) {
            userInfos.add(createdBy);
        } else {
            if (!noteDto.getCollaborator().contains(userEmail)) {
                userInfos.add(createdBy);
            }
        }
        noteInfo.setCollaborator(userInfos);
        NoteStatus status = noteStatusRepo.findByStatusText(noteDto.getNoteStatus() == null ? "Active" : noteDto.getNoteStatus());
        noteInfo.setNoteStatus(status);

        Colors colors = colorRepo.findByColorName(noteDto.getColour() == null ? "White" : noteDto.getColour());
        noteInfo.setColors(colors);

        noteInfo.setPinned(noteDto.isPinned());

        noteInfo.setShowTick(noteDto.isShowTick());

        noteInfo.setNoteRemainder(noteDto.getNoteRemainder());
        noteInfo.setNoteRemainderLocation(noteDto.getNoteRemainderLocation());
        Date date = Date.from(Instant.now());

        if (edit) {
            noteInfo.setNoteLastEditedOn(date);
            noteInfo.setEditedBy(createdBy);
        } else {
            noteInfo.setNoteCreatedOn(date);
            noteInfo.setCreatedBy(createdBy);
        }
        noteInfo = noteRepo.save(noteInfo);
        return noteInfo.getNoteId();
    }

}
