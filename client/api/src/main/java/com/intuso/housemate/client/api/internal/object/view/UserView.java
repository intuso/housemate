package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class UserView extends View {

    private CommandView renameCommandView;
    private CommandView removeCommandView;
    private PropertyView emailPropertyView;

    public UserView() {}

    public UserView(Mode mode) {
        super(mode);
    }

    public UserView(CommandView renameCommandView,
                    CommandView removeCommandView,
                    PropertyView emailPropertyView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.removeCommandView = removeCommandView;
        this.emailPropertyView = emailPropertyView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public UserView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public CommandView getRemoveCommandView() {
        return removeCommandView;
    }

    public UserView setRemoveCommandView(CommandView removeCommandView) {
        this.removeCommandView = removeCommandView;
        return this;
    }

    public PropertyView getEmailPropertyView() {
        return emailPropertyView;
    }

    public UserView setEmailPropertyView(PropertyView emailPropertyView) {
        this.emailPropertyView = emailPropertyView;
        return this;
    }
}
