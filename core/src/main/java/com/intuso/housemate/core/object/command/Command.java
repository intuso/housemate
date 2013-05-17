package com.intuso.housemate.core.object.command;

import com.intuso.housemate.core.comms.Message;
import com.intuso.housemate.core.object.BaseObject;
import com.intuso.housemate.core.object.command.argument.Argument;
import com.intuso.housemate.core.object.command.argument.HasArguments;
import com.intuso.housemate.core.object.list.List;

import java.util.Map;

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

    public void perform(Map<String, String> values, CommandListener<? super C> listener);

    public static class PerformMessageValue implements Message.Payload {

        String opId;
        Map<String, String> values;

        private PerformMessageValue() {}

        public PerformMessageValue(String opId, Map<String, String> values) {
            this.opId = opId;
            this.values = values;
        }

        public String getOpId() {
            return opId;
        }

        public Map<String, String> getValues() {
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
