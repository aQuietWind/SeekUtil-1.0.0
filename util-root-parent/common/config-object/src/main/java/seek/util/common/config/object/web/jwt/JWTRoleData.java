package seek.util.common.config.object.web.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTRoleData {
    private String headerSign;
    private String secretKey;
    private String subject;
    private Long tokenDurationMillis;
}
