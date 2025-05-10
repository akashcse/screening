package com.powerledger.screening.api;

import com.powerledger.screening.model.BatteryFilterRequest;
import com.powerledger.screening.model.BatteryFilterResponse;
import com.powerledger.screening.model.BatteryInfo;
import com.powerledger.screening.model.ErrorInfo;
import com.powerledger.screening.repository.BatteryRepository;
import com.powerledger.screening.service.contract.BatteryService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "1000000")
@Tag("Integration")
public class BatteryEndpointTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private BatteryService batteryService;
    @Autowired
    private BatteryRepository batteryRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () ->postgres.getJdbcUrl().replace("jdbc", "r2dbc"));
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        batteryRepository.deleteAll();
    }

    @DisplayName("Should add new Battery to grid")
    @Test
    public void registerBattery_expect_Battery() {
        BatteryInfo batteryInfo = setupBatteryInfo();
        Flux<BatteryInfo> batteryInfos = this.webTestClient.post()
                .uri("/batteries")
                .bodyValue(List.of(batteryInfo, batteryInfo, batteryInfo))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(BatteryInfo.class)
                .getResponseBody();

        StepVerifier.create(batteryInfos)
                .assertNext(batteryInfo1 -> {
                    Assertions.assertEquals(batteryInfo1.getName(), batteryInfo.getName());
                }).assertNext(batteryInfo1 -> {
                    Assertions.assertEquals(batteryInfo1.getPostcode(), batteryInfo.getPostcode());
                }).assertNext(batteryInfo1 -> {
                    Assertions.assertEquals(batteryInfo1.getCapacity(), batteryInfo.getCapacity());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("Should BadRequestException during add new Battery")
    @Test
    public void registerBattery_expect_BadRequestException() {
        BatteryInfo batteryInfo = setupBatteryInfo();
        batteryInfo.setName(null);
        this.webTestClient.post()
                .uri("/batteries")
                .bodyValue(Collections.singletonList(batteryInfo))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBodyList(ErrorInfo.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<ErrorInfo> errorInfos = listEntityExchangeResult.getResponseBody();
                    assertEquals("name required", errorInfos.get(0).getDescription());
                });

    }

    @DisplayName("Filter battery expect BadRequestException")
    @Test
    public void filterBattery_expect_BadRequestException() {
        BatteryFilterRequest batteryFilterRequest = new BatteryFilterRequest();
        batteryFilterRequest.setPostcodeRange(new BatteryFilterRequest.Range("2500", "2900"));
        batteryFilterRequest.setMaximumWatt(120000f);
        batteryFilterRequest.setMinimumWatt(1000f);

        this.webTestClient.post()
                .uri("/batteries/filter")
                .bodyValue(batteryFilterRequest)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBodyList(ErrorInfo.class)
                .consumeWith(batteryFilterResponseEntityExchangeResult -> {
                    List<ErrorInfo> errorInfos = batteryFilterResponseEntityExchangeResult.getResponseBody();
                    assertEquals("Either minimum or maximum is expected", errorInfos.get(0).getDescription());
                });
    }

    @DisplayName("Filter battery based on postcode range and capacity")
    @Test
    public void filterBattery_expect_BatteryFilterResponse() {
        BatteryFilterRequest batteryFilterRequest = new BatteryFilterRequest();
        batteryFilterRequest.setPostcodeRange(new BatteryFilterRequest.Range("2500", "70000"));
        batteryFilterRequest.setMaximumWatt(120000f);

        this.webTestClient.post()
                .uri("/batteries/filter")
                .bodyValue(batteryFilterRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BatteryFilterResponse.class)
                .consumeWith(batteryFilterResponseEntityExchangeResult -> {
                    BatteryFilterResponse batteryResponse = batteryFilterResponseEntityExchangeResult.getResponseBody();
                    assertNotNull(batteryResponse.getAverageWatt());
                    assertNotNull(batteryResponse.getTotalWatt());
                    assertNotNull(batteryResponse.getNames());
                });
    }

    private BatteryInfo setupBatteryInfo() {
        return new BatteryInfo("Ootha", "2875", 13500f);
    }
}
