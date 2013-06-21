package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyArgument;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentInputList extends TypeInputList<GWTProxyArgument> {
    @Override
    public TableRow createObjectTableRow(GWTProxyArgument argument) {
        return new TypeInputTableRow(argument.getId(), argument.getName(), argument.getDescription(), argument.getType(), getInstances());
    }
}
