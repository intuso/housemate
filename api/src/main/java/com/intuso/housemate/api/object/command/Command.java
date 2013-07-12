package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.HasParameters;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.TypeInstanceMap;

/**
 * @param <PL> the type of the propeties list
 * @param <C> the type of the command
 */
public interface Command<
            PL extends List<? extends Parameter<?>>,
            C extends Command<?, ?>>
        extends BaseHousemateObject<CommandListener<? super C>>, HasParameters<PL> {

    public final static String PARAMETERS_ID = "parameters";
    public final static String PERFORM_TYPE = "perform";
    public final static String PERFORMING_TYPE = "performing";
    public final static String FAILED_TYPE = "failed";

    /**
     * Performs the command
     * @param values the values of the parameters
     * @param listener the listener to call about progress of the command
     */
    public void perform(TypeInstanceMap values, CommandListener<? super C> listener);

    /**
     * Message payload for a perform command call
     */
    public static class PerformMessageValue implements Message.Payload {

        private String opId;
        private TypeInstanceMap values;

        private PerformMessageValue() {}

        /**
         * @param opId the operation id of the command used to associate progress messages with the original call
         * @param values the values to perform with
         */
        public PerformMessageValue(String opId, TypeInstanceMap values) {
            this.opId = opId;
            this.values = values;
        }

        /**
         * Gets the operation id
         * @return the operation id
         */
        public String getOpId() {
            return opId;
        }

        /**
         * Gets the values
         * @return the values
         */
        public TypeInstanceMap getValues() {
            return values;
        }
    }

    /**
     * Message payload for command progress
     */
    public static class PerformingMessageValue implements Message.Payload {

        private String opId;
        private boolean performing;

        private PerformingMessageValue() {}

        /**
         * @param opId the operation id of the original call
         * @param performing true if the command is currently in progress
         */
        public PerformingMessageValue(String opId, boolean performing) {
            this.opId = opId;
            this.performing = performing;
        }

        /**
         * Gets the operation id
         * @return the operation id
         */
        public String getOpId() {
            return opId;
        }

        /**
         * Gets if the operation is currently in progress
         * @return true if the operation is currently in progress
         */
        public boolean isPerforming() {
            return performing;
        }

        @Override
        public String toString() {
            return opId + " " + performing;
        }
    }

    /**
     * Message payload for failed commands
     */
    public static class FailedMessageValue implements Message.Payload {

        private String opId;
        private String cause;

        private FailedMessageValue() {}

        /**
         * @param opId the operation id of the original call
         * @param cause the cause of the failure
         */
        public FailedMessageValue(String opId, String cause) {
            this.opId = opId;
            this.cause = cause;
        }

        /**
         * Gets the operation id
         * @return the operation id
         */
        public String getOpId() {
            return opId;
        }

        /**
         * Gets the cause of the failure
         * @return the cause of the failure
         */
        public String getCause() {
            return cause;
        }
    }
}
