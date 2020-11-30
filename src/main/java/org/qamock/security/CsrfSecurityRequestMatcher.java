package org.qamock.security;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Component
public class CsrfSecurityRequestMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {

        if(allowedMethods.matcher(httpServletRequest.getMethod()).matches()){
            return false;
        }

        if(httpServletRequest.getRequestURI().contains("/dynamic/resource/")){
            return false;
        }

        if(httpServletRequest.getRequestURI().contains("/api/jms/")){
            return false;
        }

        if(httpServletRequest.getRequestURI().contains("/h2")){
            return false;
        }

        return true;
    }
}
