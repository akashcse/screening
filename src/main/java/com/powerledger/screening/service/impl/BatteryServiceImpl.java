package com.powerledger.screening.service.impl;

import com.powerledger.screening.entity.Battery;
import com.powerledger.screening.exception.BadRequestException;
import com.powerledger.screening.model.BatteryFilterRequest;
import com.powerledger.screening.model.BatteryFilterResponse;
import com.powerledger.screening.repository.BatteryRepository;
import com.powerledger.screening.service.contract.BatteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Service that managing Battery
 */
@Service
@Slf4j
public class BatteryServiceImpl extends ServiceBaseImpl<Battery, Long> implements BatteryService {
    private final BatteryRepository batteryRepository;

    @Autowired
    public BatteryServiceImpl(BatteryRepository batteryRepository) {
        super(batteryRepository);
        this.batteryRepository = batteryRepository;
    }

    /**
     * Filter battry based on given request
     * @param batteryFilterRequest
     * @return BatteryFilterResponse
     */
    @Override
    public Mono<BatteryFilterResponse> filter(Mono<BatteryFilterRequest> batteryFilterRequest) {
        log.debug("filter battery");
        //used to sum up all capacity
        AtomicReference<Double> total = new AtomicReference<>(0d);
        //used to count total filtered data
        AtomicReference<Integer> count = new AtomicReference<>(0);
        return batteryFilterRequest
                //minium and maximum can not exists in same request
                .filter(batteryFilter -> batteryFilter.getMaximumWatt() == null ||
                        batteryFilter.getMinimumWatt() ==null)
                .switchIfEmpty(Mono.error(new BadRequestException("Either minimum or maximum is expected")))
                .flatMapMany(batteryFilter -> {
                    if(batteryFilter.getMaximumWatt() != null) {
                        //filter by range and maximum watt
                        return batteryRepository.findByPostcodeBetweenAndCapacityLessThanEqualAndIsDeletedFalse(
                                batteryFilter.getPostcodeRange().getFrom(), batteryFilter.getPostcodeRange().getTo(),
                                batteryFilter.getMaximumWatt());
                    }
                    if(batteryFilter.getMinimumWatt() != null) {
                        //filter by range and minimum watt
                        return batteryRepository.findByPostcodeBetweenAndCapacityGreaterThanEqualAndIsDeletedFalse(
                                batteryFilter.getPostcodeRange().getFrom(), batteryFilter.getPostcodeRange().getTo(),
                                batteryFilter.getMinimumWatt());
                    }
                    //filter by range
                    return batteryRepository.findByPostcodeBetweenAndIsDeletedFalse(
                            batteryFilter.getPostcodeRange().getFrom(), batteryFilter.getPostcodeRange().getTo());

                })
                .doOnNext(battery -> total.updateAndGet(v -> v + battery.getCapacity()))
                .doOnNext(battery -> count.updateAndGet(v -> v + 1))
                //collect only name
                .map(Battery::getName)
                //sort name
                .collectSortedList()
                //prepare response
                .map(names -> new BatteryFilterResponse(names, total.get(), total.get()/count.get()));

    }
}
