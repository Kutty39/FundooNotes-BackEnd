package com.blbz.fundoonotebackend.repository;

import com.blbz.fundoonotebackend.entiry.ProfilePic;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PicRepo extends JpaRepository<ProfilePic,Integer> {
    ProfilePic findByCreatedBy(UserInfo userInfo);
}
