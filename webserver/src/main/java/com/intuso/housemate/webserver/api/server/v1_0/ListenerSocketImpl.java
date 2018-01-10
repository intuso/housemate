package com.intuso.housemate.webserver.api.server.v1_0;

import com.google.inject.Singleton;
import com.intuso.housemate.client.v1_0.rest.ListenerSocket;
import com.intuso.housemate.client.v1_0.serialisation.json.JsonSerialiser;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.interceptor.HeartbeatInterceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Singleton
@AtmosphereService(
        dispatch = true,
        interceptors = {HeartbeatInterceptor.class},
        servlet = "com.intuso.housemate.webserver.api.server.v1_0.AtmosphereServletContainer"
)
public class ListenerSocketImpl implements ListenerSocket {

    private final static String COOKIE_NAME = "HM_SERVER_SESSION";

    private final Listeners listeners;
    private final JsonSerialiser jsonSerialiser;

    @Inject
    public ListenerSocketImpl(Listeners listeners, JsonSerialiser jsonSerialiser) {
        this.listeners = listeners;
        this.jsonSerialiser = jsonSerialiser;
    }

    @Override
    public Response listen(HttpServletRequest request) {
        // manually get session id as atmosphere impl of HttpServletRequest doesn't let us access the session.
        String sessionId = (String) request.getAttribute(SessionIdInjector.SESSION_ID_KEY);
        if(sessionId != null) {
            final AtmosphereResource resource = (AtmosphereResource) request.getAttribute("org.atmosphere.cpr.AtmosphereResource");
            resource.setSerializer((os, o) -> os.write(jsonSerialiser.serialise((Serializable) o).getBytes()));
            resource.suspend(-1);
            Broadcaster broadcaster = listeners.getBroadcaster(sessionId);
            broadcaster.addAtmosphereResource(resource);
            return Response.ok().build();
        } else
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Missing required session cookie " + COOKIE_NAME)
                    .build();
    }
}
