package br.com.picpay.notification;

import org.springframework.data.relational.core.mapping.Table;

@Table(name = "NOTIFICATIONS")
public record Notification(boolean message) {

}
