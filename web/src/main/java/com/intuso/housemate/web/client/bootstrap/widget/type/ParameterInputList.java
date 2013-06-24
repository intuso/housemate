package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyParameter;

/**
 */
public class ParameterInputList extends TypeInputList<GWTProxyParameter> {
    @Override
    public TableRow createObjectTableRow(GWTProxyParameter parameter) {
        return new TypeInputTableRow(parameter.getId(), parameter.getName(), parameter.getDescription(), parameter.getType(), getInstances());
    }
}
