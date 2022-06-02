package com.bootcamp.reactive.document;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document(collection = "contracts")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contract implements Serializable{

	@Id
	private String id;
	private Long numberTransaction;
	private String methodPay;
	private Long phoneNumberBuyer;
	private Long phoneNumberSeller;
	private double amountInSoles;
	private String state;
	
	private static final long serialVersionUID = 1L;
}
