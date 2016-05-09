package com.intuso.housemate.platform.android.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.Gson;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.data.serialiser.json.config.GsonConfig;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/10/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class JsonMessage implements Parcelable {

    private final static Gson GSON = GsonConfig.createGson();

    private final Serializable payload;

    public JsonMessage(Serializable payload) {
        this.payload = payload;
    }

    public Serializable getPayload() {
        return payload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {;
        parcel.writeString(GSON.toJson(payload));
    }

    public final static Parcelable.Creator<JsonMessage> CREATOR = new Creator<JsonMessage>() {
        @Override
        public JsonMessage createFromParcel(Parcel parcel) {
            String json = parcel.readString();
            try {
                return new JsonMessage(GSON.fromJson(json, Serializable.class));
            } catch(Throwable t) {
                throw new HousemateException("Could not deserialise JSON message: " + json, t);
            }
        }

        @Override
        public JsonMessage[] newArray(int i) {
            return new JsonMessage[i];
        }
    };
}
