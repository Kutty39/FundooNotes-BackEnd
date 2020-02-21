package com.blbz.fundoonotebackend.utility;

import com.blbz.fundoonotebackend.dto.NoteDto;
import com.blbz.fundoonotebackend.entiry.Label;
import com.blbz.fundoonotebackend.entiry.NoteInfoEl;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Component
public class DtoMapper {

    public NoteDto noteDtoMapper(NoteInfoEl noteInfo) {
        NoteDto noteDto= new NoteDto();
        BeanUtils.copyProperties(noteInfo, noteDto);
        noteDto.setColour(noteInfo.getColors().getColorName());
        noteDto.setNoteStatus(noteInfo.getNoteStatus().getStatusText());
        noteDto.setCollaborator(noteInfo.getCollaborator().stream().filter(Objects::nonNull).map(UserInfo::getEid).collect(Collectors.toList()));
        noteDto.setLabels(noteInfo.getLabels().stream().filter(Objects::nonNull).map(Label::getLabelText).collect(Collectors.toList()));
        return noteDto;
    }
}
