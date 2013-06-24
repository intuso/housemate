package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.HandlerRegistration;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.handler.HasTypeInputEditedHandlers;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxySubType;

/**
 */
public class SubTypeInputList
        extends TypeInputList<GWTProxySubType>
        implements HasTypeInputEditedHandlers, TypeInputEditedHandler {

    @Override
    public TableRow createObjectTableRow(GWTProxySubType subType) {
        TypeInputTableRow result = new TypeInputTableRow(subType.getId(), subType.getName(), subType.getDescription(), subType.getType(), getInstances());
        result.addTypeInputEditedHandler(this);
        return result;
    }

    @Override
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

    @Override
    public void onTypeInputEdited(TypeInputEditedEvent event) {}
}
