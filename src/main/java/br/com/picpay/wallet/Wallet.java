package br.com.picpay.wallet;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "WALLET")
public record Wallet(
		@Id Long id,
		String fullName,
		Long cpf,
		String email,
		String password,
		WalletType type,
		BigDecimal Balance) {

}
