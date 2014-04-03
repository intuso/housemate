package com.intuso.housemate.comms.serialiser.json.mixins;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intuso.housemate.api.comms.Message;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/01/14
 * Time: 08:50
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
                fieldVisibility = JsonAutoDetect.Visibility.NONE,
                getterVisibility = JsonAutoDetect.Visibility.NONE,
                isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE)
public class MessageMixin extends Message<Message.Payload> {

    @JsonProperty
    String[] path;

    @JsonProperty
    String type;

    @JsonProperty
    Payload payload;

    @JsonProperty
    List<String> route;

    @JsonCreator
    public MessageMixin(@JsonProperty("path") String[] path,
                        @JsonProperty("type") String type,
                        @JsonProperty("payload") Payload payload,
                        @JsonProperty("route") List<String> route) {
        super(path, type, payload, route);
    }
}
