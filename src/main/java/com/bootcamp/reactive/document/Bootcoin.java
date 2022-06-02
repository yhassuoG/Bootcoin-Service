package com.bootcamp.reactive.document;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document(collection = "bootcoins")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Bootcoin implements Serializable{

	@Id
	private String id;
	private String typeDocument;
	private String document;
	private Long phoneNumber;
	private String imei;
	private String email;
	private double saldo;
	
	private static final long serialVersionUID = 1L;
}
