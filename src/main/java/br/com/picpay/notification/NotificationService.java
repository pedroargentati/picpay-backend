package br.com.picpay.notification;

import org.springframework.stereotype.Service;

import br.com.picpay.transaction.Transaction;

@Service
public class NotificationService {
	
	private NotificationProducer notificationProducer;

	public NotificationService(NotificationProducer notificationProducer) {
		this.notificationProducer = notificationProducer;
	}
	
	/**
	 * Será assíncrono pois o serviço de notificação é intermitente.
	 *  -> Se fosse síncrono a transação teria muito rollback e exceções
	 * e isso não é bom, não passa segurança para o usuário.
	 * 
	 * @param transaction transação em questão.
	 */
	public void notify(Transaction transaction) {
		notificationProducer.sendNotification(transaction);
	}
	
}
