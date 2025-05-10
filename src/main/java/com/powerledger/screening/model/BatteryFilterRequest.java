package com.powerledger.screening.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatteryFilterRequest {
    private Range postcodeRange;
    private Float minimumWatt;
    private Float maximumWatt;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Range {
        @NotNull
        private String from;
        @NotNull
        private String to;
    }
}
