package com.blbz.fundooapi.serviceimpl;

import com.blbz.fundooapi.dto.LabelDto;
import com.blbz.fundooapi.entiry.Label;
import com.blbz.fundooapi.exception.InvalidUserException;
import com.blbz.fundooapi.exception.LabelNotFoundException;
import com.blbz.fundooapi.repository.LabelRepo;
import com.blbz.fundooapi.service.JwtUtil;
import com.blbz.fundooapi.service.LabelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepo labelRepo;
    private final JwtUtil jwtUtil;
    private Label label;

    @Autowired
    public LabelServiceImpl(LabelRepo labelRepo, Label label, JwtUtil jwtUtil) {
        this.labelRepo = labelRepo;
        this.label = label;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Label createLabel(LabelDto labelDto) {
        BeanUtils.copyProperties(labelDto, label);
        return labelRepo.save(label);
    }

    @Override
    public List<Label> getAllLabels(@RequestHeader("Authorization") String jwtToken) throws  InvalidUserException {
        jwtUtil.validateHeader(jwtToken);
        return labelRepo.findAll();
    }

    @Override
    public Label getLabel(String labelText, @RequestHeader("Authorization") String jwtToken) throws LabelNotFoundException,  InvalidUserException {
        jwtUtil.validateHeader(jwtToken);
        if (labelText != null) {
            label = labelRepo.findByUniqKey(labelText);
            if(label ==null){
                throw new LabelNotFoundException();
            }
            return label;
        }
        throw new LabelNotFoundException();
    }

    @Override
    public Label editLabel(LabelDto labelDto) throws LabelNotFoundException {
        Optional<Label> label1=labelRepo.findById(labelDto.getLabelId());
        if(label1.isPresent()){
            label1.get().setLabelText(labelDto.getLabelText());
            labelRepo.save(label1.get());
            return label1.get();
        }
        throw new LabelNotFoundException();
    }

}
