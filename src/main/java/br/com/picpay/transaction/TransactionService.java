package br.com.picpay.transaction;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.picpay.wallet.Wallet;
import br.com.picpay.wallet.WalletRepository;
import br.com.picpay.wallet.WalletType;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final WalletRepository walletRepository;
	
	public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
		this.transactionRepository = transactionRepository;
		this.walletRepository = walletRepository;
	}
	
	@Transactional // se uma operação falhar, será feito o rollback de tudo.
	public Transaction create(Transaction transaction) {
		// validar a transação
		
		this.validate(transaction);
		
		// criar a transação
		
		var newTransaction = this.transactionRepository.save(transaction); 
		
		// debitar da carteira
		var wallet = walletRepository.findById(transaction.payer()).get();
		walletRepository.save(wallet.debit(transaction.value()));
		
		// chamar serviços externos (mensageria)
		
		return newTransaction;
	}

	/**
	 * A transação vai ser válida se:
	 *  - Pagador tem a carteira do tipo comum
	 *  - Pagador tem saldo suficiente.
	 *  - Pagador não pode ser o recebedor.
	 * @param transaction transação em questão.
	 */
	private void validate(Transaction transaction) {
		try {
		    // Tenta encontrar a carteira do beneficiário (payee).
		    Optional<Wallet> payeeOptional = walletRepository.findById(transaction.payee());
		    if (payeeOptional.isEmpty()) {
		        throw new InvalidTransactionException("Invalid transaction - " + transaction);
		    }
		    
		    // Tenta encontrar a carteira do pagador (payer).
		    Optional<Wallet> payerOptional = walletRepository.findById(transaction.payer());
		    if (payerOptional.isEmpty()) {
		        throw new InvalidTransactionException("Invalid transaction - " + transaction);
		    }
		    
		    Wallet payer = payerOptional.get();
		    // Verifica se o pagador é do tipo COMUM, tem saldo suficiente e não é o mesmo que o beneficiário.
		    if (payer.type().getValue() == WalletType.COMUM.getValue() &&
		        payer.balance().compareTo(transaction.value()) >= 0 &&
		        !payer.id().equals(transaction.payee())) {
		    } else {
		        throw new InvalidTransactionException("Invalid transaction - " + transaction);
		    }
		} catch (InvalidTransactionException e) {
		    throw e;
		}
	}
	
}
