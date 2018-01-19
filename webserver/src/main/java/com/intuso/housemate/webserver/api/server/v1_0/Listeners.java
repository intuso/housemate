package com.intuso.housemate.webserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.webserver.api.server.v1_0.model.TreeUpdate;
import com.intuso.utilities.collection.ManagedCollection;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.Broadcaster;

import java.util.List;
import java.util.Map;

public class Listeners {

    private final AtmosphereServlet atmosphereServlet;

    // todo clean these up when embedded broadcaster has no resources.
    private final Map<String, ListenerImpl> listeners = Maps.newConcurrentMap();
    private final Map<String, List<ManagedCollection.Registration>> listenerRegistrations = Maps.newConcurrentMap();

    @Inject
    public Listeners(AtmosphereServlet atmosphereServlet) {
        this.atmosphereServlet = atmosphereServlet;
    }

    public synchronized Broadcaster getBroadcaster(String sessionId) {
        if(listeners.containsKey(sessionId))
            return listeners.get(sessionId).broadcaster;
        else {
            Broadcaster broadcaster = atmosphereServlet.framework().getBroadcasterFactory().get(sessionId);
            listeners.put(sessionId, new ListenerImpl(broadcaster));
            listenerRegistrations.put(sessionId, Lists.newArrayList());
            return broadcaster;
        }
    }

    public Tree.Listener getListener(String sessionId) {
        // todo remove this once finished testing
        if(!listeners.containsKey(sessionId))
            getBroadcaster(sessionId);
        return listeners.get(sessionId);
    }

    public List<ManagedCollection.Registration> getListenerRegistrations(String sessionId) {
        return listenerRegistrations.get(sessionId);
    }

    public class ListenerImpl implements Tree.Listener {

        private final Broadcaster broadcaster;

        public ListenerImpl(Broadcaster broadcaster) {
            this.broadcaster = broadcaster;
        }

        @Override
        public void updated(String path, Tree tree) {
            broadcaster.broadcast(new TreeUpdate(path, tree));
        }
    }
}
