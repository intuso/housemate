package com.intuso.housemate.web.client.bootstrap.widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 16/09/13
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */
public class WidgetRow extends FlowPanel {

    private final Label titleLabel = new Label();

    public WidgetRow() {
        super.add(titleLabel);
        addStyleName("widget-row");
        addStyleName("clearfix");
        titleLabel.addStyleName("title");
    }

    public WidgetRow(String title, IsWidget widget) {
        this();
        setTitle(title);
        add(widget);
    }

    public void setTitle(String title) {
        super.setTitle(title);
        titleLabel.setText(title);
    }

    @Override
    public void add(Widget w) {
        w.getElement().addClassName("pull-right");
        super.add(w);
    }
}
