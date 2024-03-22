package com.little_pay.challenge.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cost {
    private String origin;
    private String destination;
    private Double amount;
}
