package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.web.client.handler.HasArgumentEditedHandlers;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public interface ArgumentInput extends HasArgumentEditedHandlers, IsWidget {
    public void setValue(Value<?, ?> value);
}
