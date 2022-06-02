package com.bootcamp.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.reactive.document.Bootcoin;

import reactor.core.publisher.Mono;

@Repository
public interface BootcoinRepository extends ReactiveMongoRepository<Bootcoin,String>{

	Mono<Bootcoin> findByPhoneNumber(Long phoneNumberBuyer);

	
}
