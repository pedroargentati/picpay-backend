package br.com.picpay.wallet;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wallet")
public class WalletController {

	private final WalletService walletService;
	
	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@GetMapping
	public List<Wallet> list() {
		return this.walletService.list();
	}
	
	@GetMapping("/{id}")
	public Wallet getById(@PathVariable Long id) {
		return this.walletService.getById(id);
	}
	
}
