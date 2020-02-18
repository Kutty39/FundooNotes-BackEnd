package com.blbz.fundoonotebackend.entiry;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Component
@Data
public class ProfilePic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int picID;
    private String filePath;
    @OneToOne
    private UserInfo createdBy;
}
