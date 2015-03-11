package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyPrimaryObject;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * Created by tomc on 02/03/15.
 */
public abstract class ObjectWidget<OBJECT extends ProxyObject<?, ?, ?, ?, ?>> extends Composite implements PrimaryListener<ProxyPrimaryObject<?, ?, ?, ?, ?>> {

    interface WidgetPanelUiBinder extends UiBinder<Widget, ObjectWidget> {}

    private static WidgetPanelUiBinder ourUiBinder = GWT.create(WidgetPanelUiBinder.class);

    @UiField
    Heading heading;

    @UiField
    PanelBody panelBody;

    private OBJECT object;

    public ObjectWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    protected void setName(String name) {
        if(object != null)
            heading.setText(name);
    }

    protected void setObject(OBJECT object) {
        if(object instanceof ProxyPrimaryObject)
                ((ProxyPrimaryObject) object).addObjectListener(this);
        this.object = object;
        heading.setText(object.getName());
        panelBody.clear();
        panelBody.add(getBodyWidget(object));
    }

    protected void setMessage(AlertType type, String message) {
        panelBody.clear();
        panelBody.add(new Alert(message, type));
    }

    @Override
    public void renamed(ProxyPrimaryObject<?, ?, ?, ?, ?> primaryObject, String oldName, String newName) {
        heading.setText(newName);
    }

    @Override
    public void error(ProxyPrimaryObject<?, ?, ?, ?, ?> primaryObject, String error) {

    }

    @Override
    public void running(ProxyPrimaryObject<?, ?, ?, ?, ?> primaryObject, boolean running) {

    }

    @UiHandler("settings")
    public void onEdit(ClickEvent event) {
        new SettingsModal(object.getName(), getSettingsWidget(object));
    }

    protected abstract IsWidget getBodyWidget(OBJECT object);

    protected abstract IsWidget getSettingsWidget(OBJECT object);
}
