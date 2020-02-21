package com.blbz.fundoonotebackend.entiry;

import lombok.Data;
import org.springframework.core.annotation.Order;

import javax.persistence.*;

@Entity
@Data
@Order(6)
//@Document(indexName = "webappdb",type = "colors")
public class Colors {
    @Id
    //@org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int colorId;
    @Column(unique = true,nullable = false)
    private String colorName;
}
