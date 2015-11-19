package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.object.api.internal.ApplicationInstance;

/**
 * Data object for an automation
 */
public final class ApplicationInstanceData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String ALLOW_COMMAND_ID = "allow";
    public final static String REJECT_COMMAND_ID = "reject";
    public final static String STATUS_VALUE_ID = "status";

    public ApplicationInstanceData() {}

    public ApplicationInstanceData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new ApplicationInstanceData(getId(), getName(), getDescription());
    }

    public static class StatusPayload implements Message.Payload {

        private static final long serialVersionUID = -1L;

        private ApplicationInstance.Status status;

        public StatusPayload() {}

        public StatusPayload(ApplicationInstance.Status status) {
            this.status = status;
        }

        public ApplicationInstance.Status getStatus() {
            return status;
        }

        public void setStatus(ApplicationInstance.Status status) {
            this.status = status;
        }

        @Override
        public void ensureSerialisable() {}
    }
}
