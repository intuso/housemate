package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.web.client.handler.HasTypeInputEditedHandlers;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public interface TypeInput extends HasTypeInputEditedHandlers, IsWidget {
    public void setTypeInstance(TypeInstance typeInstance);
}
