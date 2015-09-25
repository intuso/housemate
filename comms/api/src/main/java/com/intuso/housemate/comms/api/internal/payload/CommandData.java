package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;

/**
 * Data object for a command
 */
public final class CommandData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String ENABLED_ID = "enabled";
    public final static String PARAMETERS_ID = "parameters";
    public final static String PERFORM_TYPE = "perform";
    public final static String PERFORMING_TYPE = "performing";
    public final static String FAILED_TYPE = "failed";

    public CommandData() {}

    public CommandData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new CommandData(getId(), getName(), getDescription());
    }

    public static class PerformPayload implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String opId;
        private TypeInstanceMap values;

        public PerformPayload() {}

        /**
         * @param opId the operation id of the command used to associate progress messages with the original call
         * @param values the values to perform with
         */
        public PerformPayload(String opId, TypeInstanceMap values) {
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

        public void setOpId(String opId) {
            this.opId = opId;
        }

        /**
         * Gets the values
         * @return the values
         */
        public TypeInstanceMap getValues() {
            return values;
        }

        public void setValues(TypeInstanceMap values) {
            this.values = values;
        }

        @Override
        public void ensureSerialisable() {
            if(values != null)
                TypeData.TypeInstanceMapPayload.ensureSerialisable(values);
        }
    }

    /**
     * Message payload for command progress
     */
    public static class PerformingPayload implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String opId;
        private boolean performing;

        public PerformingPayload() {}

        /**
         * @param opId the operation id of the original call
         * @param performing true if the command is currently in progress
         */
        public PerformingPayload(String opId, boolean performing) {
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

        public void setOpId(String opId) {
            this.opId = opId;
        }

        /**
         * Gets if the operation is currently in progress
         * @return true if the operation is currently in progress
         */
        public boolean isPerforming() {
            return performing;
        }

        public void setPerforming(boolean performing) {
            this.performing = performing;
        }

        @Override
        public String toString() {
            return opId + " " + performing;
        }

        @Override
        public void ensureSerialisable() {}
    }

    /**
     * Message payload for failed commands
     */
    public static class FailedPayload implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private String opId;
        private String cause;

        public FailedPayload() {}

        /**
         * @param opId the operation id of the original call
         * @param cause the cause of the failure
         */
        public FailedPayload(String opId, String cause) {
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

        public void setOpId(String opId) {
            this.opId = opId;
        }

        /**
         * Gets the cause of the failure
         * @return the cause of the failure
         */
        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        @Override
        public void ensureSerialisable() {}
    }
}
