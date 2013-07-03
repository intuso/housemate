package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.dom.client.Style;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.bootstrap.widget.list.TableList;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCol;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableColGroup;

/**
 */
public abstract class TypeInputList<O extends BaseObject<?>> extends TableList<O> {

    private TypeInstanceMap instances;

    public TypeInputList() {
        addColGroup();
    }

    public void setTypeInstances(TypeInstanceMap instances) {
        this.instances = instances != null ? instances : new TypeInstanceMap();
    }

    protected TypeInstanceMap getInstances() {
        return instances;
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
}
