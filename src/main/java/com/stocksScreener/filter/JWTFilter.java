package com.stocksScreener.filter;

import com.stocksScreener.model.User;
import com.stocksScreener.repository.UserRepository;
import com.stocksScreener.utils.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException {


        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = this.jwtUtil.extractUsername(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        // Once we get the token validate it.
        if (username != null && !username.equals("") && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findUserByUserEmail(username);
            System.out.println(user);
            if (user != null) {
                if (!jwtUtil.validateToken(jwtToken, user)) {
                    System.out.println("Invalid token");
                    try {
                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.println("Valid token");
                        filterChain.doFilter(httpServletRequest, httpServletResponse);
                    } catch (IOException | ServletException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        System.out.println("Path " + request.getServletPath());
        List<String> urls = new ArrayList<>();
        urls.add("/google/login");
        return urls.stream().anyMatch(s -> s.equals(request.getServletPath()));
    }
}
