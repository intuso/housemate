package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.bridge.v1_0.object.TypeInstanceMapMapper;
import com.intuso.housemate.client.api.internal.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.client.v1_0.real.api.RealParameter;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealCommandBridgeReverse implements RealCommand<RealValue<Boolean, ?, ?>, RealList<? extends RealParameter<?, ?, ?>, ?>, RealCommandBridgeReverse> {

    private final com.intuso.housemate.client.real.api.internal.RealCommand<?, ?, ?> command;
    private final TypeInstanceMapMapper typeInstanceMapMapper;
    private final ValueMapper valueMapper;
    private final ListMapper listMapper;
    private final ParameterMapper parameterMapper;

    @Inject
    public RealCommandBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealCommand<?, ?, ?> command,
                                    TypeInstanceMapMapper typeInstanceMapMapper, ValueMapper valueMapper, ListMapper listMapper, ParameterMapper parameterMapper) {
        this.command = command;
        this.typeInstanceMapMapper = typeInstanceMapMapper;
        this.valueMapper = valueMapper;
        this.listMapper = listMapper;
        this.parameterMapper = parameterMapper;
    }

    public com.intuso.housemate.client.real.api.internal.RealCommand getCommand() {
        return command;
    }

    @Override
    public RealValue<Boolean, ?, ?> getEnabledValue() {
        return valueMapper.map(command.getEnabledValue());
    }

    @Override
    public void perform(Type.InstanceMap values) {
        command.perform(typeInstanceMapMapper.map(values));
    }

    @Override
    public void perform(Type.InstanceMap value, PerformListener<? super RealCommandBridgeReverse> listener) {
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
    public ListenerRegistration addObjectListener(Listener<? super RealCommandBridgeReverse> listener) {
        return null; // todo
    }

    @Override
    public RealList<? extends RealParameter<?, ?, ?>, ?> getParameters() {
        return listMapper.map((com.intuso.housemate.client.real.api.internal.RealList<com.intuso.housemate.client.real.api.internal.RealParameter<?, ?, ?>, ?>)command.getParameters(),
                parameterMapper.getToV1_0Function(),
                parameterMapper.getFromV1_0Function());
    }

    private class PerformListenerBridge implements Command.PerformListener<com.intuso.housemate.client.real.api.internal.RealCommand<?, ?, ?>> {

        private final PerformListener<? super RealCommandBridgeReverse> listener;

        private PerformListenerBridge(PerformListener<? super RealCommandBridgeReverse> listener) {
            this.listener = listener;
        }

        @Override
        public void commandStarted(com.intuso.housemate.client.real.api.internal.RealCommand command) {
            listener.commandStarted(RealCommandBridgeReverse.this);
        }

        @Override
        public void commandFinished(com.intuso.housemate.client.real.api.internal.RealCommand command) {
            listener.commandFinished(RealCommandBridgeReverse.this);
        }

        @Override
        public void commandFailed(com.intuso.housemate.client.real.api.internal.RealCommand command, String error) {
            listener.commandFailed(RealCommandBridgeReverse.this, error);
        }
    }

    public interface Factory {
        RealCommandBridgeReverse create(com.intuso.housemate.client.real.api.internal.RealCommand<?, ?, ?> command);
    }
}
