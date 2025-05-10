package com.powerledger.screening.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatteryFilterResponse {
    private List<String> names;
    private Double totalWatt;
    private Double averageWatt;
}
