package com.blbz.fundooapi.repository;

import com.blbz.fundooapi.entiry.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface LabelRepo extends JpaRepository<Label,Integer> {
    @Query("from Label where labelText=:text")
    Label findByUniqKey(String text);
}
