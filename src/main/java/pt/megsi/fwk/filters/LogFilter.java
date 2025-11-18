package pt.megsi.fwk.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {

    public static final String LOG_INFO_STRING = "Request Uri: [{}] Request Method: [{}] Response Status: [{}]";
    public static final String LOG_DEBUG_STRING = "Request Uri: [{}] Request Method: [{}] Response Body: [{}] Response Status: [{}]";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isMultipart = request.getContentType() != null &&
                request.getContentType().toLowerCase().startsWith("multipart/");

        HttpServletRequest wrappedRequest = request;

        if (!isMultipart) {
            wrappedRequest = new CachedBodyHttpServletRequest(request);
        }

        CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        if (!isMultipart) {
            if (!wrappedResponse.getBodyAsString().isBlank()) {
                log.info(LOG_INFO_STRING, request.getRequestURI(), request.getMethod(), wrappedResponse.getStatus());
                log.info(LOG_DEBUG_STRING, request.getRequestURI(), request.getMethod(), wrappedResponse.getBodyAsString(), wrappedResponse.getStatus());
            } else {
                log.info(LOG_INFO_STRING, request.getRequestURI(), request.getMethod(), wrappedResponse.getStatus());
            }
        } else {
            log.info(LOG_INFO_STRING, request.getRequestURI(), request.getMethod(), wrappedResponse.getStatus());
            log.info(LOG_DEBUG_STRING, request.getRequestURI(), request.getMethod(), wrappedResponse.getBodyAsString(), wrappedResponse.getStatus());
        }

        response.getOutputStream().write(wrappedResponse.getBody());
    }
}