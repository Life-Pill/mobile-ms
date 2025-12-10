package com.lifepill.notification.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to remove CORS headers added by the downstream service.
 * API Gateway is responsible for adding CORS headers.
 * This filter ensures no duplicate CORS headers are sent to the client.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RemoveCorsHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Wrap the response to intercept header setting
        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse);
        
        // Continue the filter chain
        chain.doFilter(request, wrappedResponse);
    }
    
    /**
     * Response wrapper that prevents setting CORS headers
     */
    private static class HttpServletResponseWrapper extends jakarta.servlet.http.HttpServletResponseWrapper {
        
        public HttpServletResponseWrapper(HttpServletResponse response) {
            super(response);
        }
        
        @Override
        public void setHeader(String name, String value) {
            // Skip CORS headers - API Gateway handles them
            if (isCorsHeader(name)) {
                return;
            }
            super.setHeader(name, value);
        }
        
        @Override
        public void addHeader(String name, String value) {
            // Skip CORS headers - API Gateway handles them
            if (isCorsHeader(name)) {
                return;
            }
            super.addHeader(name, value);
        }
        
        private boolean isCorsHeader(String headerName) {
            if (headerName == null) {
                return false;
            }
            String lowerCase = headerName.toLowerCase();
            return lowerCase.startsWith("access-control-") || 
                   lowerCase.equals("vary");
        }
    }
}
