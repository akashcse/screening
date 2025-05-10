package com.powerledger.screening.service;

import com.powerledger.screening.entity.Battery;
import com.powerledger.screening.exception.BadRequestException;
import com.powerledger.screening.exception.ResourceNotFoundException;
import com.powerledger.screening.model.BatteryFilterRequest;
import com.powerledger.screening.model.BatteryFilterResponse;
import com.powerledger.screening.repository.BatteryRepository;
import com.powerledger.screening.service.impl.BatteryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BatteryServiceTest {
    @Mock
    private BatteryRepository batteryRepository;
    @InjectMocks
    private BatteryServiceImpl batteryService;

    @BeforeEach
    public void init(){
        batteryService = new BatteryServiceImpl(batteryRepository);
    }

    @DisplayName("Should add new Battery to grid")
    @Test
    void createBattery_expect_Battery() {
        Battery battery = setupBattery();
        when(batteryRepository.save(any())).thenReturn(Mono.just(battery));

        Mono<Battery> response = batteryService.create(battery);
        StepVerifier.create(response)
                .assertNext(newBattery -> {
                    assertNotNull(newBattery.getId());
                    assertNotNull(newBattery.getCapacity());
                    assertNotNull(newBattery.getName());
                    assertNotNull(newBattery.getPostcode());
                }).verifyComplete();
    }

    @DisplayName("Should BadRequestException during add new Battery")
    @Test
    void createBattery_expect_BadRequestException() {
        Mono<Battery> response = batteryService.create(null);
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("Entity can not be null")
                ).verify();
    }

    @DisplayName("Should update battery info")
    @Test
    void updateBattery_expect_Battery() {
        Battery battery = setupBattery();
        battery.setName("update");
        when(batteryRepository.save(any())).thenReturn(Mono.just(battery));
        when(batteryRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Mono.just(battery));

        Mono<Battery> response = batteryService.update(1L, battery);
        StepVerifier.create(response)
                .assertNext(newBattery -> {
                    assertNotNull(newBattery.getCapacity());
                    assertNotNull(newBattery.getName());
                    assertEquals(newBattery.getName(), battery.getName());
                    assertNotNull(newBattery.getPostcode());
                }).verifyComplete();
    }

    @DisplayName("Should BadRequestException during update Battery")
    @Test
    void updateBattery_expect_BadRequestException() {
        Mono<Battery> response = batteryService.update(1L, null);
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("entity can not be null")
                ).verify();;
    }

    @DisplayName("Should get all Batteries from DB")
    @Test
    void getAllBatteries_expect_Batteries() {
        Battery battery = setupBattery();
        when(batteryRepository.findByIsDeletedFalse(any())).thenReturn(Flux.just(battery));

        Flux<Battery> response = batteryService.getAll(Pageable.unpaged());
        StepVerifier.create(response)
                .assertNext(newBattery -> {
                    assertNotNull(newBattery.getId());
                    assertNotNull(newBattery.getCapacity());
                    assertNotNull(newBattery.getName());
                    assertNotNull(newBattery.getPostcode());
                }).verifyComplete();
    }

    @DisplayName("Should get Battery by id")
    @Test
    void getBatteryById_expect_Battery() {
        Battery battery = setupBattery();
        when(batteryRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Mono.just(battery));

        Mono<Battery> response = batteryService.getById(1L);
        StepVerifier.create(response)
                .assertNext(newBattery -> {
                    assertNotNull(newBattery.getId());
                    assertNotNull(newBattery.getCapacity());
                    assertNotNull(newBattery.getName());
                    assertNotNull(newBattery.getPostcode());
                }).verifyComplete();
    }

    @DisplayName("Should ResourceNotFoundException during get Battery by id")
    @Test
    void getBatteryById_expect_ResourceNotFoundException() {
        when(batteryRepository.findByIdAndIsDeletedFalse(any()))
                .thenReturn(Mono.error(new ResourceNotFoundException("entity not found")));

        Mono<Battery> response = batteryService.getById(1L);
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof ResourceNotFoundException &&
                        throwable.getMessage().equals("entity not found")
                ).verify();;
    }

    @DisplayName("Should delete Battery by id")
    @Test
    void deleteBattery_expect_Battery() {
        Battery battery = setupBattery();
        when(batteryRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Mono.just(battery));
        when(batteryRepository.save(any())).thenReturn(Mono.just(battery));

        Mono<Battery> response = batteryService.delete(1L);
        StepVerifier.create(response)
                .assertNext(newBattery -> {
                    assertNotNull(newBattery.getId());
                    assertNotNull(newBattery.getCapacity());
                    assertNotNull(newBattery.getName());
                    assertNotNull(newBattery.getPostcode());
                }).verifyComplete();
    }

    @DisplayName("Should BadRequestException during delete Battery by id")
    @Test
    void deleteBattery_expect_BadRequestException() {
        Mono<Battery> response = batteryService.delete(null);
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("id can not be null")
                ).verify();;
    }

    @DisplayName("Should filter battery info by range")
    @Test
    void filterBattery_expect_BatteryFilterResponse() {
        Battery battery = setupBattery();
        BatteryFilterRequest batteryFilterRequest = new BatteryFilterRequest();
        batteryFilterRequest.setPostcodeRange(new BatteryFilterRequest.Range("2500", "2900"));
        when(batteryRepository.findByPostcodeBetweenAndIsDeletedFalse(any(), any()))
                .thenReturn(Flux.just(battery));

        Mono<BatteryFilterResponse> response = batteryService.filter(Mono.just(batteryFilterRequest));
        StepVerifier.create(response)
                .assertNext(batteryResponse -> {
                    assertNotNull(batteryResponse.getAverageWatt());
                    assertNotNull(batteryResponse.getTotalWatt());
                   assertNotNull(batteryResponse.getNames());
                }).verifyComplete();
    }

    @DisplayName("Should filter battery info by range with minimum capacity")
    @Test
    void filterBatteryWithMinimumCapacity_expect_BatteryFilterResponse() {
        Battery battery = setupBattery();
        BatteryFilterRequest batteryFilterRequest = new BatteryFilterRequest();
        batteryFilterRequest.setPostcodeRange(new BatteryFilterRequest.Range("2500", "2900"));
        batteryFilterRequest.setMinimumWatt(1200f);
        when(batteryRepository.findByPostcodeBetweenAndCapacityGreaterThanEqualAndIsDeletedFalse(any(), any(), any()))
                .thenReturn(Flux.just(battery));

        Mono<BatteryFilterResponse> response = batteryService.filter(Mono.just(batteryFilterRequest));
        StepVerifier.create(response)
                .assertNext(batteryResponse -> {
                    assertNotNull(batteryResponse.getAverageWatt());
                    assertNotNull(batteryResponse.getTotalWatt());
                    assertNotNull(batteryResponse.getNames());
                }).verifyComplete();
    }

    @DisplayName("Should filter battery info by range with maximum capacity")
    @Test
    void filterBatteryWithMaximumCapacity_expect_BatteryFilterResponse() {
        Battery battery = setupBattery();
        BatteryFilterRequest batteryFilterRequest = new BatteryFilterRequest();
        batteryFilterRequest.setPostcodeRange(new BatteryFilterRequest.Range("2500", "2900"));
        batteryFilterRequest.setMaximumWatt(120000f);
        when(batteryRepository.findByPostcodeBetweenAndCapacityLessThanEqualAndIsDeletedFalse(any(), any(), any()))
                .thenReturn(Flux.just(battery));

        Mono<BatteryFilterResponse> response = batteryService.filter(Mono.just(batteryFilterRequest));
        StepVerifier.create(response)
                .assertNext(batteryResponse -> {
                    assertNotNull(batteryResponse.getAverageWatt());
                    assertNotNull(batteryResponse.getTotalWatt());
                    assertNotNull(batteryResponse.getNames());
                }).verifyComplete();
    }

    @DisplayName("Filter battery info expect BadRequestException")
    @Test
    void filterBattery_expect_BadRequestException() {
        BatteryFilterRequest batteryFilterRequest = new BatteryFilterRequest(new BatteryFilterRequest.Range("2500", "2900"),
                120000f, 1000f);
        Mono<BatteryFilterResponse> response = batteryService.filter(Mono.just(batteryFilterRequest));
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("Either minimum or maximum is expected")
                ).verify();;
    }

    private Battery setupBattery() {
        Battery battery = new Battery("Ootha", "2875", 13500f);
        battery.setId(1L);
        battery.setCreatedDate(LocalDateTime.now());
        battery.setUpdatedDate(LocalDateTime.now());
        battery.setIsDeleted(false);
        return battery;
    }
}
