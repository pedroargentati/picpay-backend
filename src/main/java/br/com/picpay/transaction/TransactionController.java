package br.com.picpay.transaction;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transaction")
public class TransactionController {

	private final TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PostMapping
	public Transaction createTransaction(@RequestBody Transaction transaction) {
		return this.transactionService.create(transaction);
	}
	
	@GetMapping
	public List<Transaction> list() {
		return this.transactionService.list();
	}
	
}
