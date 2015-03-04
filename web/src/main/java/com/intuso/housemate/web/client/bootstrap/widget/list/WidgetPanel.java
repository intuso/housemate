package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;

/**
 * Created by tomc on 02/03/15.
 */
public class WidgetPanel extends Composite {

    private static int NEXT_ID = 0;

    interface WidgetPanelUiBinder extends UiBinder<Widget, WidgetPanel> {}

    private static WidgetPanelUiBinder ourUiBinder = GWT.create(WidgetPanelUiBinder.class);

    @UiField
    PanelHeader panelHeader;

    @UiField
    Heading heading;

    @UiField
    PanelCollapse panelCollapse;

    @UiField
    PanelBody panelBody;

    public WidgetPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        String id = "widgetPanel-" + NEXT_ID++;
        panelHeader.setDataTarget("#" + id);
        panelCollapse.setId(id);
    }

    public void setHeading(String name) {
        heading.setText(name);
    }

    public void setIn(boolean in) {
        panelCollapse.setIn(in);
    }

    public void addToBody(Widget widget) {
        panelBody.add(widget);
    }

    public void addShowHandler(ShowHandler showHandler) {
        panelCollapse.addShowHandler(showHandler);
    }

    public void addHideHandler(HideHandler hideHandler) {
        panelCollapse.addHideHandler(hideHandler);
    }
}
