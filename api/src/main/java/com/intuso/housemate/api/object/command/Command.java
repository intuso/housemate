package com.intuso.housemate.api.object.command;

import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.command.argument.Argument;
import com.intuso.housemate.api.object.command.argument.HasArguments;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:18
 * To change this template use File | Settings | File Templates.
 */
public interface Command<AL extends List<? extends Argument<?>>, C extends Command<?, ?>>
        extends BaseObject<CommandListener<? super C>>, HasArguments<AL> {

    public final static String ARGUMENTS_FIELD = "arguments";

    public final static String PERFORM = "perform";
    public final static String PERFORMING = "performing";
    public final static String FAILED = "failed";

    public void perform(TypeInstances values, CommandListener<? super C> listener);

    public static class PerformMessageValue implements Message.Payload {

        String opId;
        TypeInstances values;

        private PerformMessageValue() {}

        public PerformMessageValue(String opId, TypeInstances values) {
            this.opId = opId;
            this.values = values;
        }

        public String getOpId() {
            return opId;
        }

        public TypeInstances getValues() {
            return values;
        }
    }

    public static class PerformingMessageValue implements Message.Payload {

        private String opId;
        private boolean performing;

        private PerformingMessageValue() {}

        public PerformingMessageValue(String opId, boolean performing) {
            this.opId = opId;
            this.performing = performing;
        }

        public String getOpId() {
            return opId;
        }

        public boolean isPerforming() {
            return performing;
        }
    }

    public static class FailedMessageValue implements Message.Payload {

        private String opId;
        private String cause;

        private FailedMessageValue() {}

        public FailedMessageValue(String opId, String cause) {
            this.opId = opId;
            this.cause = cause;
        }

        public String getOpId() {
            return opId;
        }

        public String getCause() {
            return cause;
        }
    }
}
