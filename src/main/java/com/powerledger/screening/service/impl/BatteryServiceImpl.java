package com.powerledger.screening.service.impl;

import com.powerledger.screening.entity.Battery;
import com.powerledger.screening.repository.BatteryRepository;
import com.powerledger.screening.service.contract.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

;

@Service
public class BatteryServiceImpl extends ServiceBaseImpl<Battery, Long> implements BatteryService {
    private final BatteryRepository batteryRepository;

    @Autowired
    public BatteryServiceImpl(BatteryRepository batteryRepository) {
        super(batteryRepository);
        this.batteryRepository = batteryRepository;
    }
}
