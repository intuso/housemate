package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.message.StringPayload;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.primary.PrimaryListener;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * @param <DATA> the type of the data object
 * @param <PRIMARY_OBJECT> the type of the primary object
 * @param <LISTENER> the type of this object's listener
 */
public abstract class RealPrimaryObject<
            DATA extends HousemateData<HousemateData<?>>,
            PRIMARY_OBJECT extends RealPrimaryObject<DATA, PRIMARY_OBJECT, LISTENER>,
            LISTENER extends PrimaryListener<? super PRIMARY_OBJECT>>
        extends RealObject<DATA, HousemateData<?>, RealObject<?, ?, ?, ?>, LISTENER>
        implements PrimaryObject<RealCommand, RealCommand, RealCommand, RealValue<Boolean>, RealValue<String>, PRIMARY_OBJECT, LISTENER> {

    private final RealCommand rename;
    private final RealCommand remove;
    private final RealValue<Boolean> running;
    private final RealCommand start;
    private final RealCommand stop;
    private final RealValue<String> error;

    /**
     * @param log {@inheritDoc}
     * @param listenersFactory
     * @param data {@inheritDoc}
     * @param objectType the name of this object type, used child descriptions and log messages
     */
    public RealPrimaryObject(Log log, ListenersFactory listenersFactory, DATA data, final String objectType) {
        super(log, listenersFactory, data);
        this.rename = new RealCommand(log, listenersFactory, RENAME_ID, RENAME_ID, "Rename the " + objectType, Lists.<RealParameter<?>>newArrayList(StringType.createParameter(log, listenersFactory, NAME_ID, NAME_ID, "The new name"))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(values != null && values.getChildren().containsKey(NAME_ID)) {
                    String newName = values.getChildren().get(NAME_ID).getFirstValue();
                    if (newName != null && !RealPrimaryObject.this.getName().equals(newName)) {
                        RealPrimaryObject.this.getData().setName(newName);
                        for(LISTENER listener : RealPrimaryObject.this.getObjectListeners())
                            listener.renamed((PRIMARY_OBJECT)RealPrimaryObject.this, RealPrimaryObject.this.getName(), newName);
                        RealPrimaryObject.this.sendMessage(NEW_NAME, new StringPayload(newName));
                    }
                }
            }
        };
        this.remove = new RealCommand(log, listenersFactory, REMOVE_ID, REMOVE_ID, "Remove the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning())
                    throw new HousemateException("Cannot remove while " + objectType + " is still running");
                remove();
            }
        };
        this.running = BooleanType.createValue(log, listenersFactory, RUNNING_ID, RUNNING_ID, "Whether the " + objectType + " is running or not", false);
        this.start = new RealCommand(log, listenersFactory, START_ID, START_ID, "Start the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(!isRunning()) {
                    _start();
                    running.setTypedValues(true);
                }
            }
        };
        this.stop = new RealCommand(log, listenersFactory, STOP_ID, STOP_ID, "Stop the " + objectType, Lists.<RealParameter<?>>newArrayList()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                if(isRunning()) {
                    _stop();
                    running.setTypedValues(false);
                }
            }
        };
        this.error = StringType.createValue(log, listenersFactory, ERROR_ID, ERROR_ID, "Current error for the " + objectType, null);
        addChild(this.rename);
        addChild(this.remove);
        addChild(this.running);
        addChild(this.start);
        addChild(this.stop);
        addChild(this.error);
    }

    @Override
    public RealCommand getRenameCommand() {
        return rename;
    }

    @Override
    public RealCommand getRemoveCommand() {
        return remove;
    }

    @Override
    public RealValue<String> getErrorValue() {
        return error;
    }

    @Override
    public String getError() {
        return error.getTypedValue();
    }

    @Override
    public RealCommand getStopCommand() {
        return stop;
    }

    @Override
    public RealCommand getStartCommand() {
        return start;
    }

    @Override
    public RealValue<Boolean> getRunningValue() {
        return running;
    }

    @Override
    public boolean isRunning() {
        return running.getTypedValue() != null ? running.getTypedValue() : false;
    }

    /**
     * Removes this objects
     */
    protected abstract void remove();

    /**
     * Starts the object
     */
    protected abstract void _start();

    /**
     * Stops the object
     */
    protected abstract void _stop();
}
