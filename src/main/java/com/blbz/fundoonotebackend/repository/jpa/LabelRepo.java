package com.blbz.fundoonotebackend.repository.jpa;

import com.blbz.fundoonotebackend.entiry.Label;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface LabelRepo extends JpaRepository<Label,Integer> {
    @Query("from Label where labelText=:text")
    Label findByUniqKey(String text);
    List<Label> findByCreatedBy(UserInfo userInfo);
    Label findByCreatedByAndLabelText(UserInfo userInfo,String labelText);
    void deleteByCreatedByAndLabelText(UserInfo userInfo,String labelText);
}
