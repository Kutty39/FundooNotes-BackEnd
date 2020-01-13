package com.blbz.fundooapi.entiry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Order(6)
public class Colors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int colorId;
    @Column(unique = true,nullable = false)
    private String colorName;
  /*  @JsonIgnore
    @OneToMany
    private List<NoteInfo> noteInfos;*/
}
