package com.powerledger.screening.core;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface RepositoryBase<T extends EntityBase, P> extends ReactiveCrudRepository<T, P> {
  Flux<T> findByIsDeletedFalse(Pageable pageable);
  Mono<T> findByIdAndIsDeletedFalse(P id);
}
