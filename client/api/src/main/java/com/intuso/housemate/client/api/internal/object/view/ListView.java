package com.intuso.housemate.client.api.internal.object.view;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by tomc on 19/06/17.
 */
public class ListView<CHILD_VIEW extends View> extends View {

    private CHILD_VIEW elementView;
    private Set<String> elements = Sets.newHashSet();

    public ListView() {}

    public ListView(Mode mode) {
        super(mode);
    }

    public ListView(CHILD_VIEW elementView) {
        super(Mode.CHILDREN);
        this.elementView = elementView;
    }

    public ListView(CHILD_VIEW elementView, String... elements) {
        this(elementView, Sets.newHashSet(elements));
    }

    public ListView(CHILD_VIEW elementView, Set<String> elements) {
        super(Mode.SELECTION);
        this.elementView = elementView;
        this.elements = elements;
    }

    public CHILD_VIEW getElementView() {
        return elementView;
    }

    public ListView<CHILD_VIEW> setElementView(CHILD_VIEW elementView) {
        this.elementView = elementView;
        return this;
    }

    public Set<String> getElements() {
        return elements;
    }

    public ListView<CHILD_VIEW> setElements(Set<String> elements) {
        this.elements = elements;
        return this;
    }

    public ListView<CHILD_VIEW> addElement(String... elements) {
        if(this.elements == null)
            this.elements = Sets.newHashSet();
        this.elements.addAll(Sets.newHashSet(elements));
        return this;
    }
}
