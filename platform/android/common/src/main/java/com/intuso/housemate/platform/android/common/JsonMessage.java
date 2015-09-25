package com.intuso.housemate.platform.android.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.intuso.housemate.comms.v1_0.api.HousemateCommsException;
import com.intuso.housemate.comms.v1_0.api.Message;
import com.intuso.housemate.comms.v1_0.serialiser.json.JsonSerialiser;
import com.intuso.housemate.comms.v1_0.serialiser.json.config.GsonConfig;

import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/10/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class JsonMessage implements Parcelable {

    private final static Gson GSON = GsonConfig.createGson();

    private final Message<?> message;

    public JsonMessage(Message<?> message) {
        this.message = message;
    }

    public Message<?> getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        StringWriter sw = new StringWriter();
        GSON.toJson(message, JsonSerialiser.MESSAGE_TYPE, new JsonWriter(sw));
        parcel.writeString(sw.getBuffer().toString());
    }

    public final static Parcelable.Creator<JsonMessage> CREATOR = new Creator<JsonMessage>() {
        @Override
        public JsonMessage createFromParcel(Parcel parcel) {
            String json = parcel.readString();
            try {
                return new JsonMessage(GSON.<Message<?>>fromJson(json, JsonSerialiser.MESSAGE_TYPE));
            } catch(Throwable t) {
                throw new HousemateCommsException("Could not deserialise JSON message: " + json, t);
            }
        }

        @Override
        public JsonMessage[] newArray(int i) {
            return new JsonMessage[i];
        }
    };
}
