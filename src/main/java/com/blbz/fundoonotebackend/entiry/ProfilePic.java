package com.blbz.fundoonotebackend.entiry;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Component
@Data
//@Document(indexName = "webappdb",type = "profilepic")
public class ProfilePic {
    @Id
   // @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int picID;
    private String filePath;
    @OneToOne
    private UserInfo createdBy;
}
