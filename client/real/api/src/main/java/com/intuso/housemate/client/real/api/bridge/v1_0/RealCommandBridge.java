package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealParameter;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstanceMapMapper;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealCommandBridge implements RealCommand {

    private final com.intuso.housemate.client.v1_0.real.api.RealCommand command;
    private final TypeInstanceMapMapper typeInstanceMapMapper;
    private final ValueMapper valueMapper;
    private final ListMapper listMapper;
    private final ParameterMapper parameterMapper;

    @Inject
    public RealCommandBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealCommand command,
                             TypeInstanceMapMapper typeInstanceMapMapper, ValueMapper valueMapper, ListMapper listMapper, ParameterMapper parameterMapper) {
        this.command = command;
        this.typeInstanceMapMapper = typeInstanceMapMapper;
        this.valueMapper = valueMapper;
        this.listMapper = listMapper;
        this.parameterMapper = parameterMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealCommand getCommand() {
        return command;
    }

    @Override
    public RealValue<Boolean> getEnabledValue() {
        return valueMapper.map(command.getEnabledValue());
    }

    @Override
    public void perform(TypeInstanceMap values) {
        command.perform(typeInstanceMapMapper.map(values));
    }

    @Override
    public void perform(TypeInstanceMap value, PerformListener<? super RealCommand> listener) {
        command.perform(typeInstanceMapMapper.map(value), new PerformListenerBridge(listener));
    }

    @Override
    public String getId() {
        return command.getId();
    }

    @Override
    public String getName() {
        return command.getName();
    }

    @Override
    public String getDescription() {
        return command.getDescription();
    }

    @Override
    public String[] getPath() {
        return command.getPath();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealCommand> listener) {
        return null; // todo
    }

    @Override
    public RealList<RealParameter<?>> getParameters() {
        return listMapper.map((com.intuso.housemate.client.v1_0.real.api.RealList< com.intuso.housemate.client.v1_0.real.api.RealParameter<?>>)command.getParameters(),
                parameterMapper.getFromV1_0Function(),
                parameterMapper.getToV1_0Function());
    }

    private class PerformListenerBridge implements Command.PerformListener<com.intuso.housemate.client.v1_0.real.api.RealCommand> {

        private final PerformListener<? super RealCommand> listener;

        private PerformListenerBridge(PerformListener<? super RealCommand> listener) {
            this.listener = listener;
        }

        @Override
        public void commandStarted(com.intuso.housemate.client.v1_0.real.api.RealCommand command) {
            listener.commandStarted(RealCommandBridge.this);
        }

        @Override
        public void commandFinished(com.intuso.housemate.client.v1_0.real.api.RealCommand command) {
            listener.commandFinished(RealCommandBridge.this);
        }

        @Override
        public void commandFailed(com.intuso.housemate.client.v1_0.real.api.RealCommand command, String error) {
            listener.commandFailed(RealCommandBridge.this, error);
        }
    }

    public interface Factory {
        RealCommandBridge create(com.intuso.housemate.client.v1_0.real.api.RealCommand command);
    }
}
