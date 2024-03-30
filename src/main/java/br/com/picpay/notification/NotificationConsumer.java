package br.com.picpay.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.picpay.transaction.Transaction;

@Service
public class NotificationConsumer {

	private final RestClient restClient;
	
	public NotificationConsumer(RestClient.Builder builder) {
		this.restClient = builder
				.baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc")
				.build();
	}
	
	@KafkaListener(topics = "transaction-notificatio", groupId = "picpay-backend")
	public void receiveNotification(Transaction transaction) {
		var response = this.restClient
				.get()
				.retrieve()
				.toEntity(Notification.class);
		
		if (response.getStatusCode().isError() || !response.getBody().message()) {
			throw new NotificationException("Error seding notification!");
		}
	}
	
}
