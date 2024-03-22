package com.little_pay.challenge.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cost {
    private String origin;
    private String destination;
    private Double amount;
}
