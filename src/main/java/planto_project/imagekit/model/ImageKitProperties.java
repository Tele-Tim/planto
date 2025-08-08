package planto_project.imagekit.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "imgkit")
@Component
@Data
public class ImageKitProperties {
    private String publicKey;
    private String privateKey;
    private String urlEndpoint;
}
