package com.powerledger.screening.api;

import com.powerledger.screening.entity.Battery;
import com.powerledger.screening.model.BatteryFilterRequest;
import com.powerledger.screening.model.BatteryFilterResponse;
import com.powerledger.screening.model.BatteryInfo;
import com.powerledger.screening.model.ErrorInfo;
import com.powerledger.screening.service.contract.BatteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/batteries")
@Validated
public class BatteryEndpoint {
    private final BatteryService batteryService;
    private final ModelMapper modelMapper;

    @Operation(description = "Aggregate battery power and added to grid",
            summary = "Aggregate battery power")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Missing required properties",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BatteryInfo> aggregateBatteries(@RequestBody @Valid Flux<BatteryInfo> batteryInfos) {
        log.debug("request aggregate batteries");
        return batteryInfos
                .delayElements(Duration.ofSeconds(1))
                .map(batteryInfo -> modelMapper.map(batteryInfo, Battery.class))
                .map(batteryService::create)
                .flatMap(batteryMono -> batteryMono
                        .map(battery -> modelMapper.map(battery, BatteryInfo.class)));
    }

    @Operation(description = "Filter battery based on postcode range and capacity",
            summary = "Filter battery based on postcode and capacity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Either minimum or maximum is expected",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(schema = @Schema(implementation = ErrorInfo.class))),
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/filter")
    public Mono<BatteryFilterResponse> filterBatteries(@RequestBody @Valid Mono<BatteryFilterRequest> batteryFilterRequest) {
        log.debug("request filter batteries");
        return batteryService.filter(batteryFilterRequest);
    }
}
