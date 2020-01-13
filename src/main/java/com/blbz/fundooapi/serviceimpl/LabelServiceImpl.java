package com.blbz.fundooapi.serviceimpl;

import com.blbz.fundooapi.dto.LabelDto;
import com.blbz.fundooapi.entiry.Label;
import com.blbz.fundooapi.exception.HeaderMissingException;
import com.blbz.fundooapi.exception.InvalidUserException;
import com.blbz.fundooapi.exception.LabelNotFoundException;
import com.blbz.fundooapi.repository.LabelRepo;
import com.blbz.fundooapi.service.JwtUtil;
import com.blbz.fundooapi.service.LabelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    public List<Label> getAllLabels(HttpServletRequest request) throws HeaderMissingException, InvalidUserException {
        jwtUtil.validateHeader(request);
        return labelRepo.findAll();
    }

    @Override
    public Label getLabel(String labeltext, HttpServletRequest request) throws LabelNotFoundException, HeaderMissingException, InvalidUserException {
        jwtUtil.validateHeader(request);
        if (labeltext != null) {
            label = labelRepo.findByUniqKey(labeltext);
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
