package com.blbz.fundoonotebackend.serviceimpl;

import com.blbz.fundoonotebackend.dto.NoteDto;
import com.blbz.fundoonotebackend.dto.NoteStatusDto;
import com.blbz.fundoonotebackend.dto.NotesStatusDto;
import com.blbz.fundoonotebackend.entiry.*;
import com.blbz.fundoonotebackend.exception.*;
import com.blbz.fundoonotebackend.repository.elasticsearch.NoteElRepo;
import com.blbz.fundoonotebackend.repository.jpa.*;
import com.blbz.fundoonotebackend.service.CustomMapper;
import com.blbz.fundoonotebackend.service.JwtUtil;
import com.blbz.fundoonotebackend.service.LabelService;
import com.blbz.fundoonotebackend.service.NoteService;
import com.blbz.fundoonotebackend.utility.DtoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    private final CustomMapper customMapper;
    private final NoteRepo noteRepo;
    private final NoteElRepo noteElRepo;
    private final LabelRepo labelRepo;
    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final NoteStatusRepo noteStatusRepo;
    private final ColorRepo colorRepo;
    private final LabelService labelService;
    private final DtoMapper noteDtoMapper;
    private NoteInfo noteInfo;
    private NoteInfoEl noteInfoEl;


    @Autowired
    public NoteServiceImpl(CustomMapper customMapper, NoteRepo noteRepo, NoteElRepo noteElRepo, NoteInfo noteInfo
            , LabelRepo labelRepo, UserRepo userRepo, JwtUtil jwtUtil, NoteStatusRepo noteStatusRepo
            , ColorRepo colorRepo, LabelService labelService, DtoMapper noteDtoMapper, NoteInfoEl noteInfoEl) {
        this.customMapper = customMapper;
        this.noteRepo = noteRepo;
        this.noteElRepo = noteElRepo;
        this.noteInfo = noteInfo;
        this.labelRepo = labelRepo;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.noteStatusRepo = noteStatusRepo;
        this.colorRepo = colorRepo;
        this.labelService = labelService;
        this.noteDtoMapper = noteDtoMapper;
        this.noteInfoEl = noteInfoEl;
    }

    @Override
    public NoteDto createNote(NoteDto noteDto, String jwtHeader) throws InvalidUserException {
        return noteAction(noteDto, jwtHeader, false);
    }

    @Override
    public NoteDto editNote(NoteDto noteDto, String jwtHeader) throws InvalidUserException {
        return noteRepo.findByUniqKey(noteDto.getNoteId()) == null ? null : noteAction(noteDto, jwtHeader, true);
    }

    @Override
    public int deleteNote(int noteId, String jwtHeader) throws NoteNotFoundException, InvalidUserException {
        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        if (noteElRepo.findByCollaboratorAndNoteId(userInfo.getEid(), noteId) != null) {
            noteRepo.deleteById(noteId);
            noteElRepo.deleteById(noteId);
            return 1;
        } else {
            throw new NoteNotFoundException();
        }
    }

    @Override
    public int deleteNotes(List<Integer> noteIds, String jwtHeader) throws NoteNotFoundException, InvalidUserException {
        AtomicInteger count = new AtomicInteger();
        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        noteElRepo.findAllById(noteIds).forEach(noteInfoEl1 -> {
            if (noteElRepo.findByCollaboratorAndNoteId(userInfo.getEid(), noteInfoEl1.getNoteId()) != null) {
                noteElRepo.delete(noteInfoEl1);
                noteRepo.deleteById(noteInfoEl1.getNoteId());
                count.incrementAndGet();
            }
        });
        return count.get();
    }

    @Override
    public int updateStatus(NotesStatusDto notesStatusDto, String jwtHeader) throws InvalidUserException, InvalidNoteStatus {

        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        List<NoteInfoEl> noteInfoEls = notesStatusDto.getNoteId().stream().map(id -> noteElRepo.findByCollaboratorAndNoteId(userInfo.getEid(), id)).filter(Objects::nonNull).collect(Collectors.toList());
        NoteStatus noteStatus = noteStatusRepo.findByStatusText(notesStatusDto.getStatus());
        if (noteStatus != null) {
            List<NoteInfo> noteInfos1 = new ArrayList<>();
            for (NoteInfoEl noteInfoEl : noteInfoEls) {
                noteInfoEl.setNoteStatus(noteStatus);
                noteInfoEl.setEditedBy(userInfo);
                noteInfoEl.setNoteLastEditedOn(Date.from(Instant.now()));
                BeanUtils.copyProperties(noteInfoEl, noteInfo);
                //noteInfo.setCollaborator(noteInfoEl.getCollaborator());
                noteInfos1.add(noteInfo);


            }
            noteInfoEls = noteInfoEls.stream().map(noteInfoEl1 -> {
                noteInfoEl.setNoteStatus(noteStatus);
                noteInfoEl.setEditedBy(userInfo);
                noteInfoEl.setNoteLastEditedOn(Date.from(Instant.now()));
                return noteInfoEl;
            }).collect(Collectors.toList());
            noteRepo.saveAll(noteInfoEls.stream().map(noteInfoEl1 -> {
                NoteInfo noteInfo = new NoteInfo();
                BeanUtils.copyProperties(noteInfoEl1, noteInfo);
                return noteInfo;
            }).collect(Collectors.toList()));
            noteElRepo.saveAll(noteInfoEls);
            return noteInfoEls.size();
        }
        throw new InvalidNoteStatus();
    }

    @Override
    public int updateStatus(NoteStatusDto noteStatusDto, String jwtHeader) throws InvalidUserException, NoteNotFoundException, InvalidNoteStatus {

        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        noteInfoEl = noteElRepo.findByCollaboratorAndNoteId(userInfo.getEid(), noteStatusDto.getNoteId());
        if (noteInfoEl != null) {
            NoteStatus noteStatus = noteStatusRepo.findByStatusText(noteStatusDto.getStatus());
            if (noteStatus != null) {
                noteInfoEl.setNoteStatus(noteStatus);
                noteInfoEl.setEditedBy(userInfo);
                noteInfoEl.setNoteLastEditedOn(Date.from(Instant.now()));
                noteElRepo.save(noteInfoEl);
                BeanUtils.copyProperties(noteInfoEl,noteInfo);
                noteRepo.save(noteInfo);
                return noteInfoEl.getNoteId();
            } else {
                throw new InvalidNoteStatus();
            }
        } else {
            throw new NoteNotFoundException();
        }
    }

    @Override
    @Transactional
    public List<NoteDto> getNotesByLabel(String labelText, String jwtHeader) throws LabelNotFoundException, InvalidUserException {
        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        Label label = labelRepo.findByCreatedByAndLabelText(userInfo, labelText);
        if (label != null) {
            return noteElRepo.findByLabelsAndCollaborator(label.getLabelText(), userInfo.getEid()).stream().map(noteDtoMapper::noteDtoMapper).collect(Collectors.toList());
        }
        //throw new LabelNotFoundException("\"" + labelText + "\" not found");
        return null;
    }

    @Override
    @Transactional
    public List<NoteDto> getAllNotes(String jwtHeader) throws InvalidUserException, NoteNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        List<NoteInfoEl> noteInfoEls = noteElRepo.findByCollaborator(userInfo.getEid());
        if (noteInfoEls.size() == 0) {
            return null;
        }
        return noteInfoEls.stream().map(noteDtoMapper::noteDtoMapper).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public NoteDto getNotes(int id, String jwtHeader) throws InvalidUserException, NoteNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        NoteInfoEl noteInfo = noteElRepo.findByCollaboratorAndNoteId(userInfo.getEid(), id);
        if (noteInfo == null) {
            return null;
        }
        return noteDtoMapper.noteDtoMapper(noteInfo);
    }

    @Override
    @Transactional
    public List<NoteDto> getNotesByStatus(String statusText, String jwtHeader) throws InvalidUserException, NoteStatusNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(jwtHeader);
        NoteStatus noteStatusEl = noteStatusRepo.findByStatusText(statusText);
        if (noteStatusEl != null) {
            return noteElRepo.findByNoteStatusAndCollaborator(statusText, userInfo.getEid()).stream().map(noteDtoMapper::noteDtoMapper).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Transactional
    public List<NoteDto> getNotesByRemainder(String header) throws InvalidUserException {
        UserInfo userInfo = jwtUtil.validateHeader(header);
        return noteElRepo.findByCollaboratorAndNoteRemainderNotNull(userInfo.getEid()).stream().map(noteDtoMapper::noteDtoMapper).collect(Collectors.toList());
    }

    @Override
    public List<NoteDto> searchAll(String text, String header) throws InvalidUserException {
        UserInfo userInfo = jwtUtil.validateHeader(header);
        return noteElRepo.findAny(text,userInfo.getEid()).stream().map(noteDtoMapper::noteDtoMapper).collect(Collectors.toList());
    }

    @Override
    public NoteDto noteAction(NoteDto noteDto, String jwtHeader, boolean edit) throws InvalidUserException {
        UserInfo createdBy = jwtUtil.validateHeader(jwtHeader);
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
                    //labelDto.setLabelText(noteDto.getLabels().get(i));
                    labelList.remove(i);
                    labelList.add(i, labelService.createLabelandGet(noteDto.getLabels().get(i), jwtHeader));
                }
                ++i;
            }
        }
        noteInfo.setLabels(labelList);
        List<UserInfo> userInfos = noteDto.getCollaborator() != null ? customMapper.getListVal(noteDto.getCollaborator(), UserInfo.class, userRepo) : new ArrayList<>();
        if (noteDto.getCollaborator().size() == 0) {
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
        System.out.println(noteDto.getNoteRemainder());
        noteInfo.setNoteRemainderLocation(noteDto.getNoteRemainderLocation());
        Date date = Date.from(Instant.now());

        if (edit) {
            noteInfo.setNoteLastEditedOn(date);
            noteInfo.setEditedBy(createdBy);
        } else {
            noteInfo.setNoteCreatedOn(date);
            noteInfo.setCreatedBy(createdBy);
        }
        noteRepo.save(noteInfo);

        BeanUtils.copyProperties(noteInfo, noteInfoEl);
        noteInfoEl.setCollaborator(noteInfo.getCollaborator());
        noteElRepo.save(noteInfoEl);
        return noteDtoMapper.noteDtoMapper(noteInfoEl);
    }

}
