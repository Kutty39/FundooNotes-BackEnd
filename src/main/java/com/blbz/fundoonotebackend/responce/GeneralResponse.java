package com.blbz.fundoonotebackend.responce;

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
