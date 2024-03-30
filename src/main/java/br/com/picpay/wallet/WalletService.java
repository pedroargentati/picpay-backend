package br.com.picpay.wallet;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class WalletService {
	
	private final WalletRepository repository;
	
	public WalletService(WalletRepository repository) {
		this.repository = repository;
	}
	
	public Wallet getById(Long id) {
		if (id == null) throw new InvalidParameterException();
		
		Optional<Wallet> wallet = this.repository.findById(id);
		
		if (!wallet.isPresent()) throw new WalletNotFoundException(String.format("Wallet with id %s not found!", id));
		
		return this.repository.findById(id).get();
	}

	public List<Wallet> list() {
		return this.repository.findAll();
	}
	
}
