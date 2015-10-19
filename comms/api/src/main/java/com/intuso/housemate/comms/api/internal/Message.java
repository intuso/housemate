package com.intuso.housemate.comms.api.internal;

import com.intuso.utilities.listener.Listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to hold all details about a message sent to or received from the server
 *
 */
public class Message<T extends Message.Payload> implements Serializable {

    public final static String RECEIVED_TYPE = "received";

    private Long sequenceId;
	private String[] path;
    private String type;
    private List<String> route;
	private T payload;

    public Message() {
        path = null;
        type = null;
        route = new ArrayList<>();
    }
	
	/**
	 * Creates a new message
	 * @param path the path to the object the message is for
     * @param type the message type
	 * @param payload the message payload
	 */
    public Message(String path[], String type, T payload) {
		this(path, type, payload, null);
	}

    public Message(Long sequenceId, String path[], String type, T payload) {
        this(sequenceId, path, type, payload, null);
    }

    /**
     * Creates a new message
     * @param path the path to the object the message is for
     * @param type the message type
     * @param payload the message payload
     * @param route the route of the client the message should be sent to
     */
    public Message(String path[], String type, T payload, List<String> route) {
        this(null, path, type, payload, route);
    }

    public Message(Long sequenceId, String path[], String type, T payload, List<String> route) {
        this.sequenceId = sequenceId;
        this.path = new String[path.length];
        System.arraycopy(path, 0, this.path, 0, path.length);
        this.type = type;
        this.payload = payload;
        this.route = route != null ? route : new ArrayList<String>();
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    /**
     * Adds a client key to the route that the message is for
     * @param clientId the client id to add
     */
    public final void addClientKey(String clientId) {
        route.add(0, clientId);
    }

    /**
     * Gets the next client key, to work out who to forward the message on to
     * @return the next client key
     */
    public final String getNextClientKey() {
        return route.size() > 0 ? route.remove(0) : null;
    }

    /**
     * Gets the route of the message
     * @return the message route
     */
    public final List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

    /**
	 * Gets the path of the object the message is for
	 * @return the path of the object the message is for
	 */
	public final String[] getPath() {
		return path;
	}

    public void setPath(String[] path) {
        this.path = path;
    }

    /**
     * Gets the message type
     * @return the message type
     */
    public final String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Gets the payload
	 * @return the payload
	 */
	public final T getPayload() {
		return payload;
	}

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "{ " + (sequenceId != null ? ("seq: " + sequenceId + " ") : "") + "route: " + Arrays.toString(route.toArray()) + ", path: " + Arrays.toString(path) + ", type: " + type + ", payload: " + (payload == null ? "null" : payload.toString()) + "}";
    }

    public void ensureSerialisable() {
        if(route != null && !(route instanceof Serializable))
            route = new ArrayList<>(route);
        payload.ensureSerialisable();
    }

    /**
     * Base interface for all message payloads
     */
    public interface Payload extends Serializable {
        void ensureSerialisable();
    }

    public static class ReceivedPayload implements Payload {

        private Long sequenceId;

        public ReceivedPayload() {}

        public ReceivedPayload(Long sequenceId) {
            this.sequenceId = sequenceId;
        }

        public Long getSequenceId() {
            return sequenceId;
        }

        public void setSequenceId(Long sequenceId) {
            this.sequenceId = sequenceId;
        }

        @Override
        public void ensureSerialisable() {}
    }

    /**
     *
     * Receiver of messages from the server
     */
    public static interface Receiver<T extends Payload> extends Listener {

        /**
         * Notifies when a message has been received
         * @param message the message that was received
         */
        void messageReceived(Message<T> message);
    }

    /**
     *
     * Sender of messages to the server
     */
    public static interface Sender {

        /**
         * Sends a message to the server
         * @param message the message to send
         */
        void sendMessage(Message<?> message);
    }
}
