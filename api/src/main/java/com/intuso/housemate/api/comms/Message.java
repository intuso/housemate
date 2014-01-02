package com.intuso.housemate.api.comms;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Class to hold all details about a message sent to or received from the server
 *
 */
public class Message<T extends Message.Payload> implements Serializable {
	
	private String[] path;
    private String type;
    private List<String> route;
	private T content;

    private Message() {
        path = null;
        type = null;
        route = Lists.newLinkedList();
    }
	
	/**
	 * Creates a new message
	 * @param path the path to the object the message is for
     * @param type the message type
	 * @param payload the message payload
	 */
    public Message(String path[], String type, T payload) {
		this(path, type, payload, Lists.<String>newLinkedList());
	}

    /**
     * Creates a new message
     * @param path the path to the object the message is for
     * @param type the message type
     * @param payload the message payload
     * @param route the route of the client the message should be sent to
     */
    public Message(String path[], String type, T payload, List<String> route) {
        this.path = new String[path.length];
        System.arraycopy(path, 0, this.path, 0, path.length);
        this.type = type;
        this.content = payload;
        this.route = Lists.newLinkedList(route);
    }

    /**
     * Adds a client key to the route that the message is for
     * @param clientId the client id to add
     */
    protected final void addClientKey(String clientId) {
        route.add(0, clientId);
    }

    /**
     * Gets the next client key, to work out who to forward the message on to
     * @return the next client key
     */
    protected final String getNextClientKey() {
        return route.size() > 0 ? route.remove(0) : null;
    }

    /**
     * Gets the route of the message
     * @return the message route
     */
    public List<String> getRoute() {
        return route;
    }

    /**
	 * Gets the path of the object the message is for
	 * @return the path of the object the message is for
	 */
	public final String[] getPath() {
		return path;
	}

    /**
     * Gets the message type
     * @return the message type
     */
    public String getType() {
        return type;
    }

    /**
	 * Gets the payload
	 * @return the payload
	 */
	public final T getPayload() {
		return content;
	}
    
    @Override
    public String toString() {
        return "{ route: " + routeToString(route) + ", path: " + Arrays.toString(path) + ", type: " + type + ", content: " + (content == null ? "null" : content.toString()) + "}";
    }

    /**
     * Converts the message's route to a printable format
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String routeToString(List<T> list) {
        return "[" + Joiner.on(",").join(list) + "]";
    }

    /**
     * Base interface for all message payloads
     */
    public interface Payload extends Serializable {}
}
