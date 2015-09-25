package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.object.api.internal.Application;

/**
 * Data object for an automation
 */
public final class ApplicationData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    public final static String APPLICATION_INSTANCES_ID = "instances";
    public final static String ALLOW_COMMAND_ID = "allow";
    public final static String SOME_COMMAND_ID = "some";
    public final static String REJECT_COMMAND_ID = "reject";
    public final static String STATUS_VALUE_ID = "status";

    public ApplicationData() {}

    public ApplicationData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new ApplicationData(getId(), getName(), getDescription());
    }

    public static class StatusPayload implements Message.Payload {

        private Application.Status status;

        public StatusPayload() {}

        public StatusPayload(Application.Status status) {
            this.status = status;
        }

        public Application.Status getStatus() {
            return status;
        }

        public void setStatus(Application.Status status) {
            this.status = status;
        }

        @Override
        public void ensureSerialisable() {}
    }
}
