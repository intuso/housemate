package com.intuso.housemate.web.client.bootstrap.widget.application;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.application.ApplicationData;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectList;
import com.intuso.housemate.web.client.object.GWTProxyApplication;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 14/09/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationList extends ObjectList<ApplicationData, GWTProxyApplication> {

    public ApplicationList(String title, List<String> filteredIds, boolean includeFiltered) {
        super(Housemate.INJECTOR.getProxyRoot().getApplications(), title, filteredIds, includeFiltered);
    }

    @Override
    protected Widget getWidget(final String id, GWTProxyApplication object) {
        if(object != null)
            return new Application(object);
        else {
            final SimplePanel panel = new SimplePanel();
            Housemate.INJECTOR.getProxyRoot().getApplications().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    panel.remove(panel.getWidget());
                    panel.add(new Label("Failed to load application"));
                }

                @Override
                public void allLoaded() {
                    panel.add(new Application(Housemate.INJECTOR.getProxyRoot().getApplications().get(id)));
                }
            }, "loadApplication-" + id,
                    new HousemateObject.TreeLoadInfo(id, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
            return panel;
        }
    }
}
