package com.seek.util.configobject.JWTData;

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
