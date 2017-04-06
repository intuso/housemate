package com.intuso.housemate.pkg.server.jar.web;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyServer;
import com.intuso.housemate.webserver.SessionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by tomc on 06/04/17.
 */
public class ServerFilter implements Filter {

    private final ProxyServer.Simple server;

    @Inject
    public ServerFilter(ProxyServer.Simple server) {
        this.server = server;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest) {
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if(session != null)
                SessionUtils.setServer(session, server);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
