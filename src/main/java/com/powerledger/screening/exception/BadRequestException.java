package com.powerledger.screening.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
