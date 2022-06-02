package com.bootcamp.reactive.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.bootcamp.reactive.document.Bootcoin;
import com.bootcamp.reactive.document.Contract;
import com.bootcamp.reactive.dto.PayDto;
import com.bootcamp.reactive.repository.BootcoinRepository;
import com.bootcamp.reactive.repository.ContractRepository;
import com.bootcamp.reactive.utils.Constantes;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BootcoindService {

	@Autowired
	private BootcoinRepository repository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private StreamBridge streamBridge;

	public Mono<Bootcoin> saveBootcoin(Bootcoin bootcoin) {

		return repository.insert(bootcoin);
	}

	public Mono<Contract> sendContract(Contract contrato) {

		contrato.setState(Constantes.PENDING_STATUS);
		return contractRepository.insert(contrato);
	}

	public Flux<Contract> getAllContractsAsBuyer(Long phoneNumber) {

		return contractRepository.findAllByPhoneNumberBuyer(phoneNumber);
	}

	public Flux<Contract> getAllContractsAsSeller(Long phoneNumber) {

		return contractRepository.findAllByPhoneNumberSeller(phoneNumber);
	}

	public Mono<Contract> acceptContract(String idContract) {

		Mono<Contract> contrato = contractRepository.findById(idContract);
		Function<Contract, Contract> changeStatus = (c) -> {
			c.setState(Constantes.ACCEPTED_STATUS);
			c.setNumberTransaction(generateNumberTransaction());
			doTransaction(c);
			//contractRepository.save(c);
			return c;
		};
		return contrato.map(changeStatus).flatMap(c->{
			return contractRepository.save(c);
		});
	}

	// Se hace la transaccion en WalletService (kafka)
	public void doTransaction(Contract contrato) {

		// Primero actualizamos los saldos en yunki
		System.out.println("HACIENDO TRANSACCION...");
		PayDto pay = new PayDto();
		pay.setPhoneNumberSender(contrato.getPhoneNumberBuyer());
		pay.setPhoneNumberReceiver(contrato.getPhoneNumberSeller());
		pay.setAmount(contrato.getAmountInSoles());
		streamBridge.send("nttdata-out-0", pay);

		// Luego actualizamos los saldos en Bootcoin para el comprador
		Mono<Bootcoin> bootcoinbuyer = repository.findByPhoneNumber(contrato.getPhoneNumberBuyer());
		Function<Bootcoin, Bootcoin> updateSaldoBuyer = (b) -> {
			log.info("ACTUALIZANDO BOOTCOINS...: " + b);
			double bootcoins = contrato.getAmountInSoles() / Constantes.TASA_DE_VENTA;
			b.setSaldo(b.getSaldo() + bootcoins);
			Bootcoin boot = repository.save(b).block();
			return b;
		};
		bootcoinbuyer.map(updateSaldoBuyer).subscribe();
		
		// Y actualizamos los saldos en Bootcoin para el vendedor
		Mono<Bootcoin> bootcoinseller = repository.findByPhoneNumber(contrato.getPhoneNumberSeller());
		Function<Bootcoin, Bootcoin> updateSaldoSeller = (b) -> {
			log.info("ACTUALIZANDO BOOTCOINS...: " + b);
			double bootcoins = contrato.getAmountInSoles() / Constantes.TASA_DE_VENTA;
			b.setSaldo(b.getSaldo() - bootcoins);
			Bootcoin boot = repository.save(b).block();
			return b;
		};
		bootcoinseller.map(updateSaldoSeller).subscribe();
	}
	
	public Mono<Bootcoin> getBootcoinByPhoneNumber(Long phoneNumber){
		
		return repository.findByPhoneNumber(phoneNumber);
	}

	public Long generateNumberTransaction() {

		return (long) (Math.random() * ((10000000000000L - 99999999999999L) + 1)) + 99999999999999L;
	}
}
