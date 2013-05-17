package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.github.gwtbootstrap.client.ui.incubator.Table;
import com.intuso.housemate.core.object.BaseObject;
import com.intuso.housemate.core.object.list.List;
import com.intuso.housemate.core.object.list.ListListener;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class TableList<O extends BaseObject<?>> extends Table implements ListListener<O> {

    private Map<O, TableRow> widgetMap = new HashMap<O, TableRow>();

    public void setList(List<O> list) {
        list.addObjectListener(this, true);
    }

    @Override
    public void elementAdded(O object) {
        TableRow tr = createObjectTableRow(object);
        widgetMap.put(object, tr);
        add(tr);
    }

    @Override
    public void elementRemoved(O object) {
        TableRow tr = widgetMap.remove(object);
        if(tr != null)
            remove(tr);
    }

    public abstract TableRow createObjectTableRow(O object);
}
