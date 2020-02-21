package com.blbz.fundoonotebackend.entiry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Order(5)
@Component
//@Document(indexName = "webappdb",type = "label")
public class Label {
    @Id
   // @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int labelId;
    @Column(nullable = false)
    private String labelText;
    @ManyToOne
    @JoinColumn(name = "createdBy")
    private UserInfo createdBy;

}
