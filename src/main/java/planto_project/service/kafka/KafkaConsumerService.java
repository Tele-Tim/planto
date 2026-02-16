package planto_project.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import planto_project.dto.events.OrderCreatedEvents;
import planto_project.service.email.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${kafka.topic.order-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderCreatedEvent(
            @Payload OrderCreatedEvents event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("   Received OrderCreatedEvents from Kafka:");
        log.info("   Order ID: {}", event.getOrderId());
        log.info("   User: {}", event.getUserLogin());
        log.info("   Partition: {}, Offset: {}", partition, offset);

        try {
            emailService.sendOrderConfirmationEmail(event);

            log.info("Successfully processed OrderCreatedEvents: orderId={}", event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to process OrderCreatedEvents: orderId={}, error={}",
                    event.getOrderId(), e.getMessage(), e);

            throw e;
        }
    }
}