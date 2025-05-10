package com.powerledger.screening.model;

import com.powerledger.screening.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorInfo {
    private CodeEnum code;
    private String description;
}
