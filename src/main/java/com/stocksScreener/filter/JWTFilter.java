package com.stocksScreener.filter;

import com.stocksScreener.utils.JWTUtil;
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

    @Autowired
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException {

        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        // Once we get the token validate it.
        if (SecurityContextHolder.getContext().getAuthentication() == null && jwtToken != null) {
            try {
                if (jwtUtil.validateToken(jwtToken)) {
                    System.out.println("Valid token");
                    httpServletRequest.setAttribute("emailId", jwtUtil.extractUsername(jwtToken));
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                } else {
                    System.out.println("Invalid token");
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        System.out.println("Path " + request.getServletPath());
        List<String> urls = new ArrayList<>();
        urls.add("/google/login");
        urls.add("/stocks/price");
        return urls.stream().anyMatch(s -> s.equals(request.getServletPath()));
    }
}
