package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/10/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class SettingsModal extends Composite {

    interface SettingsModalUiBinder extends UiBinder<SimplePanel, SettingsModal> {}

    private static SettingsModalUiBinder ourUiBinder = GWT.create(SettingsModalUiBinder.class);

    @UiField
    Modal modal;

    @UiField
    ModalBody modalBody;

    public SettingsModal(String objectName, IsWidget bodyWidget) {
        initWidget(ourUiBinder.createAndBindUi(this));
        modal.setTitle(objectName);
        modalBody.add(bodyWidget);
        modal.show();
    }
}
