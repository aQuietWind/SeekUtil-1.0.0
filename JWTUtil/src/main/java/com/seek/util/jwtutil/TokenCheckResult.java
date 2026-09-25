package com.seek.util.jwtutil;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenCheckResult {
    String token;
    long resultId;
}