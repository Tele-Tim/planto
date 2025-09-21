import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class GenerateBase64String {
    public static void main(String[] args) {
                // Генерируем 512-битный ключ для HS512
                SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                String base64Key = Encoders.BASE64.encode(secretKey.getEncoded());
                System.out.println(base64Key);
    }
}
