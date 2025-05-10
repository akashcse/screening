package com.powerledger.screening.core;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceBase<T extends EntityBase, P> {
  Flux<T> getAll(Pageable pageable);

  Mono<T> getById(P id);

  Mono<T> getByIdThoDeleted(P id);

  Mono<T> create(T entity);

  Mono<T> update(P id, T entity);

  Mono<T> delete(P id);
}
