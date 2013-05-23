package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.common.base.Joiner;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.object.TreeBrowser;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/04/13
 * Time: 09:07
 * To change this template use File | Settings | File Templates.
 */
public class ObjectBrowserInput extends FlowPanel implements ArgumentInput {

    private final TextBox textBox = new TextBox();
    private final Button button = new Button("...");
    private final SingleSelectionModel<ProxyObject<?, ?, ?, ?, ?, ?, ?>> selectionModel = new SingleSelectionModel<ProxyObject<?, ?, ?, ?, ?, ?, ?>>();
    private final TreeBrowser cellTree;

    public ObjectBrowserInput(ObjectTypeWrappable typeWrappable) {
        super();
        cellTree = new TreeBrowser(selectionModel, Housemate.ENVIRONMENT.getResources().getRoot());
        cellTree.setVisible(false);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                cellTree.setVisible(true);
            }
        });
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                fireEvent(new ArgumentEditedEvent(Joiner.on("/").join(selectionModel.getSelectedObject().getPath())));
            }
        });
        add(textBox);
        add(button);
        add(cellTree);
    }

    @Override
    public void setValue(Value<?, ?> value) {
        if(value.getValue() != null) {
            HousemateObject<?, ?, ?, ?, ?> object = Housemate.ENVIRONMENT.getResources().getRoot().getWrapper(value.getValue().split("/"));
            if(object != null)
                textBox.setText(Joiner.on("/").join(object.getPath()));
            if(object instanceof ProxyObject)
                selectionModel.setSelected((ProxyObject<?, ?, ?, ?, ?, ?, ?>)object, false);
            else
                selectionModel.setSelected(null, false);
        }
    }

    @Override
    public HandlerRegistration addArgumentEditedHandler(ArgumentEditedHandler handler) {
        return addHandler(handler, ArgumentEditedEvent.TYPE);
    }
}
