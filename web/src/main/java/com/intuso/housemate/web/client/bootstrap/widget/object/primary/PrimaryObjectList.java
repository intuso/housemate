package com.intuso.housemate.web.client.bootstrap.widget.object.primary;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.object.proxy.ProxyPrimaryObject;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;
import java.util.Map;

/**
 * Created by tomc on 26/02/15.
 */
public abstract class PrimaryObjectList<
            DATA extends HousemateData<HousemateData<?>>,
            OBJECT extends ProxyPrimaryObject<DATA, ?, ?, ?, ?>>
        extends ObjectList<DATA, OBJECT>
        implements ListListener<OBJECT>, PrimaryListener<OBJECT> {

    private final Map<String, ListenerRegistration> listenerRegistrations = Maps.newHashMap();

    public PrimaryObjectList(GWTProxyList<DATA, OBJECT> list, String title, List<String> filteredIds, boolean includeFiltered) {
        super(list, title, filteredIds, includeFiltered);
        list.addObjectListener(this);
    }

    @Override
    public void elementAdded(OBJECT element) {
        listenerRegistrations.put(element.getId(), ((PrimaryObject) element).addObjectListener(this));
    }

    @Override
    public void elementRemoved(OBJECT element) {
        if(listenerRegistrations.containsKey(element.getId()))
            listenerRegistrations.remove(element.getId()).removeListener();
    }

    @Override
    public void renamed(OBJECT primaryObject, String oldName, String newName) {
        childRenamed(primaryObject.getId(), newName);
    }

    @Override
    public void error(OBJECT primaryObject, String error) {

    }

    @Override
    public void running(OBJECT primaryObject, boolean running) {

    }
}
