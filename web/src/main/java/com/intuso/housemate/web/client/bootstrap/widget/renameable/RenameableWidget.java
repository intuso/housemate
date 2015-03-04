package com.intuso.housemate.web.client.bootstrap.widget.renameable;

import org.gwtbootstrap3.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.Renameable;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandPopup;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

public class RenameableWidget extends Composite {

    interface RenameableWidgetUiBinder extends UiBinder<Widget, RenameableWidget> {}

    private static RenameableWidgetUiBinder ourUiBinder = GWT.create(RenameableWidgetUiBinder.class);

    private Renameable<GWTProxyCommand> object;

    @UiField
    public Button button;

    public RenameableWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setObject(Renameable<GWTProxyCommand> object) {
        this.object = object;
    }

    @UiHandler("button")
    public void buttonClicked(ClickEvent e) {
        button.setEnabled(false);
        new CommandPopup(object.getRenameCommand());
        object.getRenameCommand().perform(new CommandPerformListener<GWTProxyCommand>() {
            @Override
            public void commandStarted(GWTProxyCommand command) {
                // do nothing
            }

            @Override
            public void commandFinished(GWTProxyCommand command) {
                button.setEnabled(true);
            }

            @Override
            public void commandFailed(GWTProxyCommand command, String error) {
                // todo notify the failure
                button.setEnabled(true);
            }
        });
    }
}
