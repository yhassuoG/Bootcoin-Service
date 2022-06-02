package com.bootcamp.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.reactive.document.Bootcoin;
import com.bootcamp.reactive.document.Contract;
import com.bootcamp.reactive.service.BootcoindService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bootcoin")
public class BootcoinController {

	@Autowired
	private BootcoindService service;
	
	@PostMapping
    public Mono<Bootcoin> saveBootcoin(@RequestBody Bootcoin bootcoin){
        return service.saveBootcoin(bootcoin);
    }
	
	@PostMapping("/contract")
    public Mono<Contract> sendContract(@RequestBody Contract contrato){
        return service.sendContract(contrato);
    }
	
	@GetMapping("/contracts/buyer/{phoneNumber}")
    public Flux<Contract> getAllContractsAsBuyer(@PathVariable Long phoneNumber){
        return service.getAllContractsAsBuyer(phoneNumber);
    }
	
	@GetMapping("/contracts/seller/{phoneNumber}")
    public Flux<Contract> getAllContractsAsSeller(@PathVariable Long phoneNumber){
        return service.getAllContractsAsSeller(phoneNumber);
    }
	
	@PostMapping("/contract/accept/{idContrato}")
    public Mono<Contract> acceptContract(@PathVariable String idContrato){
        return service.acceptContract(idContrato);
    }
	
	@GetMapping("/{phoneNumber}")
	public Mono<Bootcoin> getBootcoinByPhoneNumber(@PathVariable Long phoneNumber){
        return service.getBootcoinByPhoneNumber(phoneNumber);
    }
}
