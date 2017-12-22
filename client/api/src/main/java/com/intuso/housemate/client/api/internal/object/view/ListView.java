package com.intuso.housemate.client.api.internal.object.view;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by tomc on 19/06/17.
 */
public class ListView<CHILD_VIEW extends View> extends View {

    private CHILD_VIEW view;
    private Set<String> elements = Sets.newHashSet();

    public ListView() {}

    public ListView(Mode mode) {
        super(mode);
    }

    public ListView(CHILD_VIEW view) {
        super(Mode.CHILDREN);
        this.view = view;
    }

    public ListView(CHILD_VIEW view, String... elements) {
        this(view, Sets.newHashSet(elements));
    }

    public ListView(CHILD_VIEW view, Set<String> elements) {
        super(Mode.SELECTION);
        this.view = view;
        this.elements = elements;
    }

    public CHILD_VIEW getView() {
        return view;
    }

    public ListView<CHILD_VIEW> setView(CHILD_VIEW view) {
        this.view = view;
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
