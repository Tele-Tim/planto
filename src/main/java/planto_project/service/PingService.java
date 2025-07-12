package planto_project.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PingService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String RENDER_URL = "https://planto-gp2i.onrender.com/product";

    @PostConstruct
    public void init() {
        ping();
    }

    @Scheduled(fixedDelay = 30000)
    private void ping() {
        try{
            var response = restTemplate.getForEntity(RENDER_URL, String.class);
            log.info("Ping successful, status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.warn("Ping failed: {}", e.getMessage());
        }

    }

}
