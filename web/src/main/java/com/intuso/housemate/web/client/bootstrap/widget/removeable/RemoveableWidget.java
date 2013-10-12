package com.intuso.housemate.web.client.bootstrap.widget.removeable;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.RemoveableObject;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

public class RemoveableWidget extends Composite {

    interface RemoveableWidgetUiBinder extends UiBinder<Widget, RemoveableWidget> {}

    private static RemoveableWidgetUiBinder ourUiBinder = GWT.create(RemoveableWidgetUiBinder.class);

    private RemoveableObject<GWTProxyCommand> object;

    @UiField
    public Button button;

    public RemoveableWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setObject(RemoveableObject<GWTProxyCommand> object) {
        this.object = object;
    }

    @UiHandler("button")
    public void buttonClicked(ClickEvent e) {
        button.setEnabled(false);
        object.getRemoveCommand().perform(new CommandListener<GWTProxyCommand>() {
            @Override
            public void commandStarted(GWTProxyCommand command) {
                // do nothing
            }

            @Override
            public void commandFinished(GWTProxyCommand command) {
                // do nothing, object is now removed
            }

            @Override
            public void commandFailed(GWTProxyCommand command, String error) {
                // todo notify the failure
                button.setEnabled(true);
            }
        });
    }
}
