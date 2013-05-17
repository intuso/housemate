package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.common.collect.Lists;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.proxy.ProxyObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/05/13
 * Time: 09:17
 * To change this template use File | Settings | File Templates.
 */
public class TreeBrowser extends CellTree {

    public TreeBrowser(SelectionModel<ProxyObject<?, ?, ?, ?, ?, ?, ?>> selectionModel,
                       ProxyObject<?, ?, ?, ?, ?, ?, ?> treeRoot) {
        super(new Model(selectionModel), treeRoot);
    }

    private static class Model implements TreeViewModel {

        private final SelectionModel<ProxyObject<?, ?, ?, ?, ?, ?, ?>> selectionModel;

        private Model(SelectionModel<ProxyObject<?, ?, ?, ?, ?, ?, ?>> selectionModel) {
            this.selectionModel = selectionModel;
        }

        @Override
        public <T> NodeInfo<?> getNodeInfo(T value) {

            ObjectCell renderer = new ObjectCell();

            ListDataProvider<ProxyObject<?, ?, ?, ?, ?, ?, ?>> dataProvider = new ListDataProvider<ProxyObject<?, ?, ?, ?, ?, ?, ?>>();
            List<ProxyObject<?, ?, ?, ?, ?, ?, ?>> children = Lists.newArrayList();
            for(ProxyObject<?, ?, ?, ?, ?, ?, ?> child : ((ProxyObject<?, ?, ?, ?, ?, ?, ?>)value).getWrappers())
                children.add(child);
            Collections.sort(children, new Comparator<HousemateObject<?, ?, ?, ?, ?>>() {
                @Override
                public int compare(HousemateObject<?, ?, ?, ?, ?> o1, HousemateObject<?, ?, ?, ?, ?> o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            dataProvider.setList(children);

            // Return a node info that pairs the data with a cell.
            return new DefaultNodeInfo<ProxyObject<?, ?, ?, ?, ?, ?, ?>>(dataProvider, renderer, selectionModel, null);
        }

        @Override
        public boolean isLeaf(Object value) {
            return !(value instanceof HousemateObject) || ((HousemateObject)value).getWrappers().size() == 0;
        }
    }

    private static class ObjectCell extends AbstractCell<ProxyObject<?, ?, ?, ?, ?, ?, ?>> {
        @Override
        public void render(Context context, ProxyObject<?, ?, ?, ?, ?, ?, ?> value, SafeHtmlBuilder sb) {
            sb.appendEscaped(value.getName());
        }
    }
}
