package com.intuso.housemate.web.client.bootstrap.widget.removeable;

import org.gwtbootstrap3.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.Removeable;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

public class RemoveableWidget extends Composite {

    interface RemoveableWidgetUiBinder extends UiBinder<Widget, RemoveableWidget> {}

    private static RemoveableWidgetUiBinder ourUiBinder = GWT.create(RemoveableWidgetUiBinder.class);

    private Removeable<GWTProxyCommand> object;

    @UiField
    public Button button;

    public RemoveableWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setObject(Removeable<GWTProxyCommand> object) {
        this.object = object;
    }

    @UiHandler("button")
    public void buttonClicked(ClickEvent e) {
        button.setEnabled(false);
        object.getRemoveCommand().perform(new CommandPerformListener<GWTProxyCommand>() {
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
