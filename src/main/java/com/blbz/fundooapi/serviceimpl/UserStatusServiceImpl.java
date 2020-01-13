package com.blbz.fundooapi.serviceimpl;

import com.blbz.fundooapi.entiry.UserStatus;
import com.blbz.fundooapi.repository.UserStatusRepo;
import com.blbz.fundooapi.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    private UserStatusRepo userStatusRepo;

    @Autowired
    UserStatusServiceImpl(UserStatusRepo userStatusRepo) {
        this.userStatusRepo = userStatusRepo;
    }

    @Override
    public boolean create(UserStatus user_status) {
        userStatusRepo.save(user_status);
        return true;
    }

    @Override
    public boolean update(UserStatus user_status) {
        UserStatus userstatus = userStatusRepo.findByStatusText(user_status.getStatusText());
        if (userstatus!=null) {
            user_status.setStatusId(userstatus.getStatusId());
            userStatusRepo.save(user_status);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserStatus> getAll(int pageNum, int count) {
        return userStatusRepo.findAll(PageRequest.of(pageNum, count,Sort.by("status_id")));
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatus getById(int id) {
        Optional<UserStatus> byId = userStatusRepo.findById(id);
        return byId.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatus getByStatus(String status) {
        return userStatusRepo.findByStatusText(status);
    }

}
