package com.powerledger.screening.repository;


import com.powerledger.screening.core.RepositoryBase;
import com.powerledger.screening.entity.Battery;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BatteryRepository extends RepositoryBase<Battery, Long> {
    Flux<Battery> findByPostcodeBetweenAndCapacityLessThanEqualAndIsDeletedFalse(String from, String to, Float maximumWatt);

    Flux<Battery> findByPostcodeBetweenAndCapacityGreaterThanEqualAndIsDeletedFalse(String from,String to, Float minimumWatt);

    Flux<Battery> findByPostcodeBetweenAndIsDeletedFalse(@NotNull String from, @NotNull String to);
}
