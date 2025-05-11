package com.powerledger.screening.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Filter for added requestId for better tracing
 */
@Component
public class RequestIdFilter implements WebFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //checking if client send requestId
        String requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }
        String finalRequestId = requestId;

        return chain.filter(exchange)
                .doFirst(() -> MDC.put(MDC_REQUEST_ID_KEY, finalRequestId))
                .contextWrite(context -> context.put(MDC_REQUEST_ID_KEY, finalRequestId))
                .doFinally(signalType -> MDC.remove(MDC_REQUEST_ID_KEY));
    }
}