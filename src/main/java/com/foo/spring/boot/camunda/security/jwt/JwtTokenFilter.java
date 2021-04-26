package com.foo.spring.boot.camunda.security.jwt;

import com.foo.spring.boot.camunda.exception.AppCommonException;
import com.foo.spring.boot.camunda.exception.EnumErrorCode;
import com.foo.spring.boot.camunda.model.User;
import com.foo.spring.boot.camunda.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.util.EngineUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(JwtTokenFilter.class);

    private JwtTokenProvider tokenProvider;

    public JwtTokenFilter(JwtTokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        try{
            String jwt = getJwt(request);
            if((jwt == null) || !tokenProvider.validateJwtToken(jwt))
                throw new AppCommonException(EnumErrorCode.ERROR_JWT_TOKEN_INVALID, "Invalid token");
            else{
                Jws<Claims> claims = tokenProvider.getSubjectFromJwtToken(jwt);

                User user = tokenProvider.getCurrentUser(claims);

                UserDetails userDetails = CustomUserDetails.build(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                ProcessEngine engine = EngineUtil.lookupProcessEngine("default");

                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                String username;

                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                try {
                    engine.getIdentityService().setAuthentication(username, getCurrentUserGroups());
                } finally {
                    clearAuthentication(engine);
                }
            }
        }catch(AppCommonException e){
            logger.debug("An error occured while parsing the jwt token!");
        }
        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "").trim();
        }
        return null;
    }

    @Override
    public void destroy() {
    }

    private void clearAuthentication(ProcessEngine engine) {
        engine.getIdentityService().clearAuthentication();
    }

    private List<String> getCurrentUserGroups(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getGroups();
    }
}

