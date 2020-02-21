package com.blbz.fundoonotebackend.entiry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Order(3)
//@Document(indexName = "webappdb",type = "userstatus")
public class UserStatus implements Serializable {
    @Id
    //@org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;
    @Column(nullable = false,unique = true)
    private String statusText;
    @JsonIgnore
    @OneToMany(mappedBy = "userStatus")
    private List<UserInfo> userInfos;
}
