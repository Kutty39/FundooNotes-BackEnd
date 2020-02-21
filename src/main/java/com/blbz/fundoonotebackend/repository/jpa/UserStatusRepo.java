package com.blbz.fundoonotebackend.repository.jpa;

import com.blbz.fundoonotebackend.entiry.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserStatusRepo extends JpaRepository<UserStatus,Integer> {
    UserStatus findByStatusText(String statusText);
}
