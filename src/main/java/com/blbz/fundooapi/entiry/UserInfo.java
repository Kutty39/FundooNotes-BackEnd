package com.blbz.fundooapi.entiry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Order(2)
@Component
public class UserInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "userFname", nullable = false)
    private String fname;
    @Column(name = "userLname", nullable = false)
    private String lname;
    @Column(name = "userEmail", unique = true, nullable = false)
    private String eid;
    @Column(name = "userPhn", nullable = false)
    private String phn;
    @Column(name = "userAddress", nullable = false)
    private String adrs;
    @JsonIgnore
    @Column(name = "userPass", nullable = false)
    private String pas;
    private Date userCreatedOn;
    private Date userLastModifiedOn;
    @ManyToOne
    @JoinColumn(name = "userStatus")
    private UserStatus userStatus;
   /* @JsonIgnore
    @ManyToMany(mappedBy = "collaborator")
    private List<NoteInfo> noteInfo;*/
}

