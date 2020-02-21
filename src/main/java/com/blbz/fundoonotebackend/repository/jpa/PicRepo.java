package com.blbz.fundoonotebackend.repository.jpa;

import com.blbz.fundoonotebackend.entiry.ProfilePic;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PicRepo extends JpaRepository<ProfilePic,Integer> {
    ProfilePic findByCreatedBy(UserInfo userInfo);
}
