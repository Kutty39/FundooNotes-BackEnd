package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.entiry.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface UserStatusService {
    boolean create(UserStatus user_status);
    boolean update(UserStatus user_status);
    Page<UserStatus> getAll(int pageNum, int count);
    UserStatus getById(int id);
    UserStatus getByStatus(String status);
}
