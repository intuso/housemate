package com.intuso.housemate.webserver;

import com.intuso.housemate.client.v1_0.proxy.object.ProxyServer;
import com.intuso.housemate.webserver.database.model.User;

import javax.servlet.http.HttpSession;

/**
 * Created by tomc on 12/02/17.
 */
public class SessionUtils {

    public final static String USER = "housemate.user";
    public final static String SERVER = "housemate.server";

    public static User getUser(HttpSession session) {
        return session != null ? (User) session.getAttribute(USER) : null;
    }

    public static void setUser(HttpSession session, User user) {
        if(session != null)
            session.setAttribute(USER, user);
    }

    public static ProxyServer.Simple getServer(HttpSession session) {
        return session != null ? (ProxyServer.Simple) session.getAttribute(SERVER) : null;
    }

    public static void setServer(HttpSession session, ProxyServer.Simple server) {
        if(session != null)
            session.setAttribute(SERVER, server);
    }
}
