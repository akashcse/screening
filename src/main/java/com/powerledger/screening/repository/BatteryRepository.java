package com.powerledger.screening.repository;


import com.powerledger.screening.core.RepositoryBase;
import com.powerledger.screening.entity.Battery;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryRepository extends RepositoryBase<Battery, Long> {
}
