package com.intuso.housemate.web.client.bootstrap.widget.table;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class TableHead extends ComplexPanel {

    private TableCellElement th;

    public TableHead() {
        Document doc = Document.get();
        th = doc.createTHElement();
        setElement(th);
    }

    public TableHead(String title) {
        this();
        getElement().setInnerText(title);
    }

    @Override
    public void add(Widget child) {
        add(child, (Element) th.cast());
    }
}
