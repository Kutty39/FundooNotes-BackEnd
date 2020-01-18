package com.blbz.fundoonotebackend.entiry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Order(4)
public class NoteStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;
    @Column(nullable = false,unique = true)
    private String statusText;

}
