package com.intuso.housemate.webserver.api.server.v1_0;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionIdInjector implements Filter {

    public final static String SESSION_ID_KEY = SessionIdInjector.class.getName() + ".SESSION_ID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            if(httpServletRequest.getSession(false) != null) {
                httpServletRequest.setAttribute(SESSION_ID_KEY, httpServletRequest.getSession().getId());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
