package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.comms.v1_0.api.payload.SubTypeData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxySubType;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Modal;

/**
 */
public class CompoundTypeInputModal extends Composite implements TypeInput {

    interface CompoundTypeInputModalUiBinder extends UiBinder<SimplePanel, CompoundTypeInputModal> {}

    private static CompoundTypeInputModalUiBinder ourUiBinder = GWT.create(CompoundTypeInputModalUiBinder.class);

    @UiField
    Modal modal;

    @UiField
    SubTypeList subTypeList;

    public CompoundTypeInputModal(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyList<SubTypeData, GWTProxySubType> list, TypeInstances typeInstances) {
        initWidget(ourUiBinder.createAndBindUi(this));
        modal.setTitle(list.getName());
        subTypeList.setTypes(types);
        subTypeList.setTypeInstances(typeInstances);
        subTypeList.setList(list);
        modal.show();
    }

    @Override
    public TypeInstances getTypeInstances() {
        return subTypeList.getTypeInstances();
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return subTypeList.addUserInputHandler(handler);
    }
}
