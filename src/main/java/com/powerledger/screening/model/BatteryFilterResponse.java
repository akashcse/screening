package com.powerledger.screening.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatteryFilterResponse {
    private List<String> names;
    private Double totalWatt;
    private Double averageWatt;
}
