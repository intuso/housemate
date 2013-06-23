package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyParameter;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class ParameterInputList extends TypeInputList<GWTProxyParameter> {
    @Override
    public TableRow createObjectTableRow(GWTProxyParameter parameter) {
        return new TypeInputTableRow(parameter.getId(), parameter.getName(), parameter.getDescription(), parameter.getType(), getInstances());
    }
}
