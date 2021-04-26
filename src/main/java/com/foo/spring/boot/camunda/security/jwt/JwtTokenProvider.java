package com.foo.spring.boot.camunda.security.jwt;

import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.EnumErrorCode;
import com.foo.spring.boot.camunda.model.User;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration}")
    private int jwtExpiration;

    public boolean validateJwtToken(String token) throws AppCommonException {
        logger.debug("Validating token ...");
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e.getMessage());
            throw new AppCommonException(EnumErrorCode.ERROR_JWT_SIGNATURE_INVALID, "Token");
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e.getMessage());
            throw new AppCommonException(EnumErrorCode.ERROR_JWT_TOKEN_INVALID, "Token");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e.getMessage());
            throw new AppCommonException(EnumErrorCode.ERROR_JWT_TOKEN_EXPIRED, "Token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
            throw new AppCommonException(EnumErrorCode.ERROR_JWT_TOKEN_UNSUPPORTED, "Token");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e.getMessage());
            throw new AppCommonException(EnumErrorCode.ERROR_JWT_TOKEN_EMPTY_CLAIMS, "Token");
        }
    }

    public Jws<Claims> getSubjectFromJwtToken(String token){
        logger.debug("Extracting subject from token ...");
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);
    }

    public User getCurrentUser(Jws<Claims> claims){
        String username = claims.getBody().get("username").toString();
        String groupsArray = claims.getBody().get("groups").toString();
        return new User(username, extractGroups(groupsArray));
    }

    public List<String> extractGroups(String groupsArray) {
        String groupsString = groupsArray.substring(0, groupsArray.length()-1);
        return Arrays.stream(groupsString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

}
