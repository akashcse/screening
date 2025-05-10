package com.powerledger.screening.model;

import com.powerledger.screening.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorInfo {
    private CodeEnum code;
    private String description;
}
