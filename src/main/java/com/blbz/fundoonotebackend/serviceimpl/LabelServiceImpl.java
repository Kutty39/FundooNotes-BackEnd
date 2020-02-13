package com.blbz.fundoonotebackend.serviceimpl;

import com.blbz.fundoonotebackend.entiry.Label;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.LabelNotFoundException;
import com.blbz.fundoonotebackend.repository.LabelRepo;
import com.blbz.fundoonotebackend.service.JwtUtil;
import com.blbz.fundoonotebackend.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public String createLabel(String labelText,String jwtToken) throws InvalidUserException {
        UserInfo userInfo=jwtUtil.validateHeader(jwtToken);
        label.setLabelText(labelText);
        label.setCreatedBy(userInfo);
        return labelRepo.save(label).getLabelText();
    }

    @Override
    public Label createLabelandGet(String labelText,String jwtToken) throws InvalidUserException {
        UserInfo userInfo=jwtUtil.validateHeader(jwtToken);
        label.setLabelText(labelText);
        label.setCreatedBy(userInfo);
        return labelRepo.save(label);
    }

    @Override
    public List<String> getAllLabels(String jwtToken) throws  InvalidUserException {
        UserInfo userInfo=jwtUtil.validateHeader(jwtToken);
        return labelRepo.findByCreatedBy(userInfo).stream().map(Label::getLabelText).collect(Collectors.toList());
    }

    @Override
    public String getLabel(String labelText, String jwtToken) throws LabelNotFoundException,  InvalidUserException {
        UserInfo userInfo=jwtUtil.validateHeader(jwtToken);
        if (labelText != null) {
            label = labelRepo.findByCreatedByAndLabelText(userInfo,labelText);
            if(label ==null){
                throw new LabelNotFoundException();
            }
            return label.getLabelText();
        }
        throw new LabelNotFoundException();
    }

    @Override
    public String editLabel(String oldLabel,String newLabel,String jwtToken) throws LabelNotFoundException, InvalidUserException {
        UserInfo userInfo=jwtUtil.validateHeader(jwtToken);
        Optional<Label> label1= Optional.ofNullable(labelRepo.findByCreatedByAndLabelText(userInfo,oldLabel));
        if(label1.isPresent()){
            label1.get().setLabelText(newLabel);
            labelRepo.save(label1.get());
            return newLabel;
        }
        throw new LabelNotFoundException();
    }

    @Override
    public void deleteLabel(String labelText, String jwtToken) throws InvalidUserException {
        UserInfo userInfo=jwtUtil.validateHeader(jwtToken);
        labelRepo.deleteByCreatedByAndLabelText(userInfo,labelText);
    }

}
