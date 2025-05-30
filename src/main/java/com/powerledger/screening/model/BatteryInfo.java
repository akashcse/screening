package com.powerledger.screening.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.powerledger.screening.core.ModelBase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BatteryInfo extends ModelBase<Long> {
    @NotBlank(message = "name required")
    private String name;
    @NotBlank(message = "postcode required")
    private String postcode;
    @NotNull(message = "capacity required")
    private Float capacity;
}
