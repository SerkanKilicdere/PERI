package com.serkan.peri.configuration.securityconfiguration;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private ValidateToken jwtAuthenticationToken;

    @Autowired
    private UsersDetailsService usersDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtAuthenticationTokenInHttpRequestHeader = request.getHeader("Authorization");
      try { if(Objects.nonNull(jwtAuthenticationTokenInHttpRequestHeader) && jwtAuthenticationTokenInHttpRequestHeader.startsWith("Bearer ")){
            String jwtAuthenticationTokenfromHttpRequestHeader = jwtAuthenticationTokenInHttpRequestHeader.substring(7);
            Optional<UUID> userId=jwtAuthenticationToken.validateToken(jwtAuthenticationTokenfromHttpRequestHeader);

            if(userId.isPresent()){
                UserDetails userDetails = usersDetailsService.loadUserById(userId.get());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                        UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }}}
            catch (Exception exception){
                logger.error("hata:  " + exception.getMessage());
            }
        
        filterChain.doFilter(request,response);
    }
}
