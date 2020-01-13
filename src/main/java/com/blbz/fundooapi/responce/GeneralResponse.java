package com.blbz.fundooapi.responce;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@ToString
public class GeneralResponse {
    private Object response;
}
