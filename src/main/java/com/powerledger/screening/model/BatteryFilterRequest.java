package com.powerledger.screening.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatteryFilterRequest {
    private Range postcodeRange;
    private Float minimumWatt;
    private Float maximumWatt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Range {
        @NotNull
        private String from;
        @NotNull
        private String to;
    }
}
