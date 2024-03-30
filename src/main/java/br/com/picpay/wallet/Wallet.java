package br.com.picpay.wallet;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "WALLETS")
public record Wallet(
		@Id Long id,
		String fullName,
		Long cpf,
		String email,
		String password,
		WalletType type,
		BigDecimal balance) {

	public Wallet debit(BigDecimal value) {
		return new Wallet(id, fullName, cpf, email, password, type, balance.subtract(value));
	}

}
