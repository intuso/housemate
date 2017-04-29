package com.intuso.housemate.client.api.internal.object;

import java.io.Serializable;

/**
 * @param <ENABLED_VALUE> the type of the value for the enabled status of the command
 * @param <PARAMETERS> the type of the propeties list
 * @param <COMMAND> the type of the command
 */
public interface Command<
            DATA_TYPE,
            ENABLED_VALUE extends Value<?, ?, ?>,
            PARAMETERS extends List<? extends Parameter<?, ?>, ?>,
            COMMAND extends Command<?, ?, ?, ?>>
        extends
        Object<Command.Listener<? super COMMAND>>,
        Parameter.Container<PARAMETERS> {

    String ENABLED_ID = "enabled";
    String PARAMETERS_ID = "parameter";
    String PERFORM_ID = "perform";
    String PERFORM_STATUS_ID = "performStatus";

    ENABLED_VALUE getEnabledValue();

    /**
     * Performs the command
     * @param value the values of the parameters
     * @param listener the listener to call about progress of the command
     */
    void perform(DATA_TYPE value, PerformListener<? super COMMAND> listener);

    /**
     *
     * Listener interface for command
     */
    interface Listener<COMMAND extends Command<?, ?, ?, ?>> extends Object.Listener {

        void commandEnabled(COMMAND command, boolean enabled);

        /**
         * Notifies that a command has been started
         * @param command the command that was started
         * @param userId the id of the user who started the command
         */
        void commandStarted(COMMAND command, String userId);

        /**
         * Notifies that a command has finished successfully
         * @param command the command that finished
         */
        void commandFinished(COMMAND command);

        /**
         * Notifies that a command failed to finish successfully
         * @param command the command that failed
         * @param error the cause of the failure
         */
        void commandFailed(COMMAND command, String error);
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 24/01/14
     * Time: 08:27
     * To change this template use File | Settings | File Templates.
     */
    interface PerformListener<COMMAND extends Command<?, ?, ?, ?>> {

        /**
         * Notifies that a command has been started
         * @param command the command that was started
         */
        void commandStarted(COMMAND command);

        /**
         * Notifies that a command has finished successfully
         * @param command the command that finished
         */
        void commandFinished(COMMAND command);

        /**
         * Notifies that a command failed to finish successfully
         * @param command the command that failed
         * @param error the cause of the failure
         */
        void commandFailed(COMMAND command, String error);
    }

    /**
     *
     * Interface to show that the implementing object has a list of commands
     */
    interface Container<COMMANDS extends Iterable<? extends Command<?, ?, ?, ?>>> {

        /**
         * Gets the commands list
         * @return the commands list
         */
        COMMANDS getCommands();
    }

    /**
     * Data object for a command
     */
    final class Data extends Object.Data {

        private static final long serialVersionUID = -1L;

        public final static String OBJECT_CLASS = "command";

        public Data() {}

        public Data(String id, String name, String description) {
            super(OBJECT_CLASS, id, name, description);
        }
    }

    final class PerformData implements Serializable {

        private String opId;
        private Type.InstanceMap instanceMap;

        public PerformData() {}

        public PerformData(String opId, Type.InstanceMap instanceMap) {
            this.opId = opId;
            this.instanceMap = instanceMap;
        }

        public String getOpId() {
            return opId;
        }

        public void setOpId(String opId) {
            this.opId = opId;
        }

        public Type.InstanceMap getInstanceMap() {
            return instanceMap;
        }

        public void setInstanceMap(Type.InstanceMap instanceMap) {
            this.instanceMap = instanceMap;
        }
    }

    final class PerformStatusData implements Serializable {

        private String opId;
        private boolean finished;
        private String error;

        public PerformStatusData() {}

        public PerformStatusData(String opId, boolean finished, String error) {
            this.opId = opId;
            this.finished = finished;
            this.error = error;
        }

        public String getOpId() {
            return opId;
        }

        public void setOpId(String opId) {
            this.opId = opId;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
