package planto_project.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import planto_project.dto.events.OrderCreatedEvents;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderCreatedEvents> kafkaTemplate;

    @Value("${kafka.topic.order-created}")
    private String orderCreatedTopic;


    public void sendOrderCreatedEvent(OrderCreatedEvents event) {
        log.info("Sending OrderCreatedEvent to Kafka: orderId={}, userLogin={}",
                event.getOrderId(), event.getUserLogin());

        CompletableFuture<SendResult<String, OrderCreatedEvents>> future =
                kafkaTemplate.send(orderCreatedTopic, event.getOrderId(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully sent OrderCreatedEvent: orderId={}, partition={}, offset={}",
                        event.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send OrderCreatedEvent: orderId={}, error={}",
                        event.getOrderId(), ex.getMessage(), ex);
            }
        });
    }
}