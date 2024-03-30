package br.com.picpay.transaction;

@SuppressWarnings("serial")
public class InvalidTransactionException extends RuntimeException {

	public InvalidTransactionException(String message) {
		super(message);
	}
	
}
