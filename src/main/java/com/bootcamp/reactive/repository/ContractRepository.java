package com.bootcamp.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.reactive.document.Contract;

import reactor.core.publisher.Flux;

@Repository
public interface ContractRepository extends ReactiveMongoRepository<Contract,String>{

	Flux<Contract> findAllByPhoneNumberBuyer(Long phoneNumber);

	Flux<Contract> findAllByPhoneNumberSeller(Long phoneNumber);

}
