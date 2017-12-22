package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class UserView extends View {

    private CommandView renameCommand;
    private CommandView removeCommand;
    private PropertyView emailProperty;

    public UserView() {}

    public UserView(Mode mode) {
        super(mode);
    }

    public UserView(CommandView renameCommand,
                    CommandView removeCommand,
                    PropertyView emailProperty) {
        super(Mode.SELECTION);
        this.renameCommand = renameCommand;
        this.removeCommand = removeCommand;
        this.emailProperty = emailProperty;
    }

    public CommandView getRenameCommand() {
        return renameCommand;
    }

    public UserView setRenameCommand(CommandView renameCommand) {
        this.renameCommand = renameCommand;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public UserView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }

    public PropertyView getEmailProperty() {
        return emailProperty;
    }

    public UserView setEmailProperty(PropertyView emailProperty) {
        this.emailProperty = emailProperty;
        return this;
    }
}
