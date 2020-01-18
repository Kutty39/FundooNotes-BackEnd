package com.blbz.fundoonotebackend.repository;

import com.blbz.fundoonotebackend.entiry.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ColorRepo extends JpaRepository<Colors,Integer> {
    Colors findByColorName(String name);
}
