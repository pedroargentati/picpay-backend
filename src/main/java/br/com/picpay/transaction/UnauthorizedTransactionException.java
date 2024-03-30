package br.com.picpay.transaction;

@SuppressWarnings("serial")
public class UnauthorizedTransactionException extends RuntimeException {

	public UnauthorizedTransactionException(String message) {
		super(message);
	}
	
}
