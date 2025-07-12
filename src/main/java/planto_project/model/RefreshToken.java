package planto_project.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    private String id;
    @Field("token_hash")
    @Indexed(unique = true)
    private String tokenHash;
    @Indexed(expireAfter = "0")
    private Instant expiryDate;
    private boolean revoked;
    @Field("user_login")
    @Indexed
    private String userLogin;
}
