package com.powerledger.screening.service.impl;

import com.powerledger.screening.entity.Battery;
import com.powerledger.screening.exception.BadRequestException;
import com.powerledger.screening.model.BatteryFilterRequest;
import com.powerledger.screening.model.BatteryFilterResponse;
import com.powerledger.screening.repository.BatteryRepository;
import com.powerledger.screening.service.contract.BatteryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class BatteryServiceImpl extends ServiceBaseImpl<Battery, Long> implements BatteryService {
    private final BatteryRepository batteryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BatteryServiceImpl(BatteryRepository batteryRepository, ModelMapper modelMapper) {
        super(batteryRepository);
        this.batteryRepository = batteryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<BatteryFilterResponse> filter(Mono<BatteryFilterRequest> batteryFilterRequest) {
        AtomicReference<Double> total = new AtomicReference<>(0d);
        AtomicReference<Integer> count = new AtomicReference<>(0);
        return batteryFilterRequest
                .doOnNext(batteryFilter -> {
                    if(batteryFilter.getMaximumWatt() != null && batteryFilter.getMinimumWatt() !=null) {
                        Mono.error(new BadRequestException("Either minimum or maximum is expected"));
                    }
                })
                .flatMapMany(batteryFilter -> {
                    if(batteryFilter.getMaximumWatt() != null) {
                        return batteryRepository.findByPostcodeBetweenAndCapacityLessThanEqualAndIsDeletedFalse(
                                batteryFilter.getPostcodeRange().getFrom(), batteryFilter.getPostcodeRange().getTo(),
                                batteryFilter.getMaximumWatt());
                    }
                    if(batteryFilter.getMinimumWatt() != null) {
                        return batteryRepository.findByPostcodeBetweenAndCapacityGreaterThanEqualAndIsDeletedFalse(
                                batteryFilter.getPostcodeRange().getFrom(), batteryFilter.getPostcodeRange().getTo(),
                                batteryFilter.getMinimumWatt());
                    }
                    return batteryRepository.findByPostcodeBetweenAndIsDeletedFalse(
                            batteryFilter.getPostcodeRange().getFrom(), batteryFilter.getPostcodeRange().getTo());

                })
                .doOnNext(battery -> total.updateAndGet(v -> v + battery.getCapacity()))
                .doOnNext(battery -> count.updateAndGet(v -> v + 1))
                .map(Battery::getName)
                .collectSortedList()
                .map(names -> new BatteryFilterResponse(names, total.get(), total.get()/count.get()));

    }
}
