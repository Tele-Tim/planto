package planto_project.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import planto_project.dto.events.OrderCreatedEvents;

import java.time.format.DateTimeFormatter;


@Service
@Slf4j
public class EmailService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public void sendOrderConfirmationEmail(OrderCreatedEvents event) {
        log.info("=".repeat(80));
        log.info("SENDING EMAIL NOTIFICATION");
        log.info("=".repeat(80));
        log.info("To: {}", event.getUserEmail());
        log.info("Subject: Order Confirmation - Order #{}", event.getOrderId());
        log.info("");
        log.info("Email Content:");
        log.info("-".repeat(80));
        log.info("");
        log.info("Dear {},", event.getUserLogin());
        log.info("");
        log.info("Thank you for your order! Your order has been successfully created.");
        log.info("");
        log.info("Order Details:");
        log.info("  Order ID: {}", event.getOrderId());
        log.info("  Order Date: {}", event.getOrderDate().format(DATE_FORMATTER));
        log.info("  Payment Method: {}", event.getPaymentMethod());
        log.info("");
        log.info("Items:");

        event.getItems().forEach(item -> {
            double itemTotal = item.getPriceUnit() * item.getQuantity();
            log.info("  - {} x {} @ ${} = ${}",
                    item.getProductName(),
                    item.getQuantity(),
                    String.format("%.2f", item.getPriceUnit()),
                    String.format("%.2f", itemTotal));
        });

        log.info("");
        log.info("  Total Amount: ${}", String.format("%.2f", event.getTotalAmount()));
        log.info("");
        log.info("We will notify you when your order is ready for delivery.");
        log.info("");
        log.info("Best regards,");
        log.info("Planto Team");
        log.info("");
        log.info("-".repeat(80));
        log.info("Email sent successfully to {}", event.getUserEmail());
        log.info("=".repeat(80));
    }

    /*
    private final JavaMailSender mailSender;

    public void sendOrderConfirmationEmailReal(OrderCreatedEvents event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getUserEmail());
            helper.setSubject("Order Confirmation - Order #" + event.getOrderId());
            helper.setText(buildEmailContent(event), true); // true = HTML

            mailSender.send(message);
            log.info("Email sent successfully to {}", event.getUserEmail());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", event.getUserEmail(), e.getMessage());
        }
    }

    private String buildEmailContent(OrderCreatedEvents event) {
        // Thymeleaf
        return "<html>...</html>";
    }
    */
}