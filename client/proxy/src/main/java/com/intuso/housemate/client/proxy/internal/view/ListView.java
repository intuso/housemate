package com.intuso.housemate.client.proxy.internal.view;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by tomc on 19/06/17.
 */
public class ListView<CHILD_VIEW extends View> extends View<ListView<CHILD_VIEW>> {

    private CHILD_VIEW defaultView;
    private List<Entry<CHILD_VIEW>> childViews = Lists.newArrayList();

    public ListView() {}

    public ListView(Mode mode) {
        super(mode);
    }

    public ListView(CHILD_VIEW defaultView, Entry<CHILD_VIEW>... childViews) {
        this(defaultView, Lists.newArrayList(childViews));
    }

    public ListView(CHILD_VIEW defaultView, List<Entry<CHILD_VIEW>> childViews) {
        super(Mode.SELECTION);
        this.defaultView = defaultView;
        this.childViews = childViews;
    }

    public CHILD_VIEW getDefaultView() {
        return defaultView;
    }

    public ListView<CHILD_VIEW> setDefaultView(CHILD_VIEW defaultView) {
        this.defaultView = defaultView;
        return this;
    }

    public List<Entry<CHILD_VIEW>> getChildViews() {
        return childViews;
    }

    public ListView<CHILD_VIEW> setChildViews(List<Entry<CHILD_VIEW>> childViews) {
        this.childViews = childViews;
        return this;
    }

    public ListView<CHILD_VIEW> addChildView(String pattern, CHILD_VIEW view) {
        if(this.childViews == null)
            childViews = Lists.newArrayList();
        this.childViews.add(new Entry<>(pattern, view));
        return this;
    }

    public static class Entry<CHILD_VIEW extends View> {

        private String pattern;
        private CHILD_VIEW view;

        public Entry() {}

        public Entry(String pattern, CHILD_VIEW view) {
            this.pattern = pattern;
            this.view = view;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public CHILD_VIEW getView() {
            return view;
        }

        public void setView(CHILD_VIEW view) {
            this.view = view;
        }
    }
}
