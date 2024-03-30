package br.com.picpay.authorization;

public record Authorization(String message) {

	public boolean isAuthorized() {
		return message.equalsIgnoreCase("Autorizado");
	}
	
}
