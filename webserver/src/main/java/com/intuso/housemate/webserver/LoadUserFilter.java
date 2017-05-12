package com.intuso.housemate.webserver;

import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.database.model.User;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by tomc on 12/05/17.
 */
public class LoadUserFilter implements Filter {

    private final Database database;

    @Inject
    public LoadUserFilter(Database database) {
        this.database = database;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            if(session != null && SessionUtils.getUser(session) == null) {
                String userId = com.intuso.utilities.webserver.oauth.SessionUtils.getUserId(session);
                if(userId != null) {
                    User user = database.getUser(userId);
                    if (user != null)
                        SessionUtils.setUser(session, user);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
