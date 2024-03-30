package br.com.picpay.transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.picpay.authorization.AuthorizerService;
import br.com.picpay.notification.NotificationService;
import br.com.picpay.wallet.Wallet;
import br.com.picpay.wallet.WalletRepository;
import br.com.picpay.wallet.WalletType;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final WalletRepository walletRepository;
	private final AuthorizerService authorizerService;
	private final NotificationService notificationService;
	
	public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService) {
		this.transactionRepository = transactionRepository;
		this.walletRepository = walletRepository;
		this.authorizerService = authorizerService;
		this.notificationService = notificationService;
	}

	@Transactional // se uma operação falhar, será feito o rollback de tudo.
	public Transaction create(Transaction transaction) {
		// validar a transação
		this.validate(transaction);
		
		// criar a transação
		var newTransaction = this.transactionRepository.save(transaction); 
		
		// debitar da carteira
		var wallet = this.walletRepository.findById(transaction.payer()).get();
		this.walletRepository.save(wallet.debit(transaction.value()));
		
		// chamar serviços externos (mensageria)
		
		// --> autorização de transações
		this.authorizerService.authorize(transaction);
		
		// --> notificação
		this.notificationService.notify(transaction);
		
		return newTransaction;
	}
	
	public List<Transaction> list() {
		return transactionRepository.findAll();
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
			Optional<Wallet> payeeOptional = this.walletRepository.findById(transaction.payee());
			if (payeeOptional.isEmpty()) {
				throw new InvalidTransactionException("Invalid transaction - " + transaction);
			}

			// Tenta encontrar a carteira do pagador (payer).
			Optional<Wallet> payerOptional = this.walletRepository.findById(transaction.payer());
			if (payerOptional.isEmpty()) {
				throw new InvalidTransactionException("Invalid transaction - " + transaction);
			}

			Wallet payer = payerOptional.get();
			// Verifica se o pagador é do tipo COMUM, tem saldo suficiente e não é o mesmo que o beneficiário.
			if (payer.type().getValue() == WalletType.COMUM.getValue()
					&& payer.balance().compareTo(transaction.value()) >= 0 
					&& !payer.id().equals(transaction.payee())) {
			} else {
				throw new InvalidTransactionException("Invalid transaction - " + transaction);
			}
		} catch (InvalidTransactionException e) {
			throw e;
		}
	}

}
