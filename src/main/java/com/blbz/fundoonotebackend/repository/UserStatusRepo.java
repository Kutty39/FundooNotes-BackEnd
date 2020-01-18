package com.blbz.fundoonotebackend.repository;

import com.blbz.fundoonotebackend.entiry.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepo extends JpaRepository<UserStatus,Integer> {
    UserStatus findByStatusText(String statusText);
}
