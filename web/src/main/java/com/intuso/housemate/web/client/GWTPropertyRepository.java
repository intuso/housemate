package com.intuso.housemate.web.client;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.intuso.utilities.collection.ListenersFactory;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 25/01/14
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class GWTPropertyRepository extends PropertyRepository {

    @Inject
    public GWTPropertyRepository(ListenersFactory managedCollectionFactory) {
        super(managedCollectionFactory, null);
    }

    @Override
    protected void _set(String key, String value) {
        Cookies.setCookie(key, value);
    }

    @Override
    protected void _remove(String key) {
        Cookies.removeCookie(key);
    }

    @Override
    protected Set<String> _keySet() {
        return Sets.newHashSet(Cookies.getCookieNames());
    }

    @Override
    protected String _get(String key) {
        return Cookies.getCookie(key);
    }
}
