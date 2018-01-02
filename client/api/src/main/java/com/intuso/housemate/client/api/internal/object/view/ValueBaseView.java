package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class ValueBaseView extends View {

    private Boolean watch;

    public ValueBaseView() {}

    public ValueBaseView(Mode mode) {
        super(mode);
    }

    public Boolean getWatch() {
        return watch;
    }

    public void setWatch(Boolean watch) {
        this.watch = watch;
    }
}
