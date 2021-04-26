package com.foo.spring.boot.camunda.util;

import com.foo.spring.boot.camunda.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private final static String jwtSecret = "0A7A690F19001C887DEB108AE40C54717E740385243437A863569C91BF8AC99B";
    private final static long jwtExpiration = 86400000;

    public static String generateJwtToken(User user){

        // Build additional claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());

        StringBuilder groupsString = new StringBuilder();
        for(String group: user.getGroups()){
            groupsString.append(group);
            groupsString.append(",");
        }
        claims.put("groups", groupsString.toString());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public static void main(String[] args) {
        //User simon = new User("simon", Arrays.asList("camunda-admin"));
        User jan = new User("jan", Arrays.asList("other-user"));
        System.out.println(generateJwtToken(jan));
    }
}
