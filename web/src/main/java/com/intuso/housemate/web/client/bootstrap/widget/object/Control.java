package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.Removeable;
import com.intuso.housemate.api.object.Runnable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.WidgetRow;
import com.intuso.housemate.web.client.bootstrap.widget.removeable.RemoveableWidget;
import com.intuso.housemate.web.client.bootstrap.widget.runnable.RunnableWidget;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class Control extends Composite {

    interface ControlUiBinder extends UiBinder<Widget, Control> {}

    private static ControlUiBinder ourUiBinder = GWT.create(ControlUiBinder.class);

    @UiField
    WidgetRow runnableRow;
    @UiField
    RunnableWidget runnableWidget;
    @UiField
    WidgetRow removeableRow;
    @UiField
    RemoveableWidget removeableWidget;

    public Control(ProxyObject object) {
        initWidget(ourUiBinder.createAndBindUi(this));

        if(object instanceof Runnable) {
            runnableRow.setVisible(true);
            runnableWidget.setObject((Runnable<GWTProxyCommand, GWTProxyValue>) object);
        }

        if(object instanceof Removeable) {
            removeableRow.setVisible(true);
            removeableWidget.setObject((Removeable<GWTProxyCommand>) object);
        }
    }
}
