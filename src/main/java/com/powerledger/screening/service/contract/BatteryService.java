package com.powerledger.screening.service.contract;


import com.powerledger.screening.core.ServiceBase;
import com.powerledger.screening.entity.Battery;
import com.powerledger.screening.model.BatteryFilterRequest;
import com.powerledger.screening.model.BatteryFilterResponse;
import reactor.core.publisher.Mono;

public interface BatteryService extends ServiceBase<Battery, Long> {
    Mono<BatteryFilterResponse> filter(Mono<BatteryFilterRequest> batteryFilterRequest);
}
