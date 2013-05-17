package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.constants.ResponsiveStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.proxy.ProxyObject;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectNavs;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.place.HousematePlace;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 25/11/12
 * Time: 00:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class ObjectListView<O extends ProxyObject<?, ?, ?, ?, ?, ?, ?>, P extends HousematePlace> extends Composite
        implements ObjectSelectedHandler<O> {

    interface ObjectListViewUiBinder extends UiBinder<Widget, ObjectListView> {
    }

    private static ObjectListViewUiBinder ourUiBinder = GWT.create(ObjectListViewUiBinder.class);

    @UiField
    ObjectNavs listContainer;
    @UiField
    Column selectedObjectContainer;

    protected final GWTResources<?> resources;
    private GWTProxyList<?, O> list;
    private O selectedObject;
    private P place;

    public ObjectListView(GWTResources<?> resources) {
        this.resources = resources;
        initWidget(ourUiBinder.createAndBindUi(this));
        listContainer.addObjectSelectedHandler(this);
    }

    public void newPlace(P place) {

        this.place = place;

        // remove previous selected object view
        selectedObjectContainer.clear();

        // get the new list and give it to the list object
        list = getList(place);
        listContainer.setList(list, getAddCommand(place));

        // if an object is selected, then show it
        if(getSelectedObjectName(place) != null)
            selectObject(list.get(getSelectedObjectName(place)));
        else
            selectObject(null);
    }

    protected abstract Widget getObjectWidget(P place, O object);
    protected abstract GWTProxyList<?, O> getList(P place);
    protected abstract GWTProxyCommand getAddCommand(P place);
    protected abstract String getSelectedObjectName(P place);
    protected abstract P getPlace(P place, O object);

    @Override
    public void objectSelected(ObjectSelectedEvent<O> event) {
        selectObject(event.getObject());
        place = getPlace(place, event.getObject());
        History.newItem(Housemate.FACTORY.getPlaceHistoryMapper().getToken(place));
    }

    private void selectObject(O object) {

        listContainer.highlightObject(object);

        // clear selected object view
        selectedObjectContainer.clear();

        // select new object
        selectedObject = object;
        if(selectedObject != null) {
            selectedObjectContainer.add(getObjectWidget(place, object));
            selectedObjectContainer.setVisible(true);
            listContainer.addResponsiveStyle(ResponsiveStyle.HIDDEN_PHONE);
        } else
            listContainer.removeResponsiveStyle(ResponsiveStyle.HIDDEN_PHONE);
    }
}
