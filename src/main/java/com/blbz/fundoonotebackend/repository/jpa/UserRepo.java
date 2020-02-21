package com.blbz.fundoonotebackend.repository.jpa;

import com.blbz.fundoonotebackend.entiry.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<UserInfo, Integer> {
    @Query("from UserInfo where eid=:email")
    UserInfo findByUniqKey(String email);
}
