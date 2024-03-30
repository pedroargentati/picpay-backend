package br.com.picpay.wallet;

public class WalletNotFoundException extends RuntimeException {
	
	public WalletNotFoundException(String message) {
		super(message);
	}

}
