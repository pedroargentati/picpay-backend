package br.com.picpay.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.picpay.transaction.Transaction;

@Service
public class NotificationProducer {

	private final KafkaTemplate<String, Transaction> kafkaTemplate;
	
	private final String TRANSACTION_TOPIC = "transaction-notificatio";
	
	public NotificationProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	/**
	 * Coloca a mensagem de notificação no tópico (Kafka).
	 * @param transaction
	 */
	public void sendNotification(Transaction transaction) {
		this.kafkaTemplate.send(this.TRANSACTION_TOPIC, transaction);
	}
	
}
