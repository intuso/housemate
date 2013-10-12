package com.intuso.housemate.web.client.handler;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 30/09/13
 * Time: 18:57
 * To change this template use File | Settings | File Templates.
 */
public class MultiListSelectedIdsChangedHandler implements SelectedIdsChangedHandler {

    private final Set<Set<String>> sets = Sets.newHashSet();
    private final SelectedIdsChangedHandler callback;

    public MultiListSelectedIdsChangedHandler(SelectedIdsChangedHandler callback) {
        this.callback = callback;
    }

    @Override
    public void selectedIdsChanged(Set<String> ids) {
        sets.add(ids);
        Set<String> combined = Sets.newHashSet();
        for(Set<String> set : sets)
            combined.addAll(set);
        callback.selectedIdsChanged(combined);
    }
}
