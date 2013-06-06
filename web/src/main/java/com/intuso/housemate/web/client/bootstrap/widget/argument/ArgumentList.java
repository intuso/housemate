package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.google.gwt.dom.client.Style;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.bootstrap.widget.list.TableList;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCol;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableColGroup;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyArgument;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentList extends TableList<GWTProxyArgument> {

    private TypeInstances values;

    public ArgumentList() {
        addColGroup();
    }

    public void setTypeValues(TypeInstances values) {
        this.values = values;
    }

    private void addColGroup() {
        TableColGroup colGroup= new TableColGroup();
        TableCol titleCol = new TableCol();
        titleCol.getElement().getStyle().setWidth(100, Style.Unit.PX);
        TableCol valueCol = new TableCol();
        colGroup.add(titleCol);
        colGroup.add(valueCol);
        add(colGroup);
    }

    @Override
    public TableRow createObjectTableRow(GWTProxyArgument argument) {
        return new ArgumentTableRow(argument, values);
    }
}
