package com.intuso.housemate.api.comms;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Class to hold all details about a message sent to or received from the broker
 * @author tclabon
 *
 */
public class Message<T extends Message.Payload> implements Serializable {
	
	/**
	 * The message path
	 */
	private String[] path;

    private String type;

    private List<String> route;
	
	/**
	 * The actual message itself
	 */
	private T content;

    private Message() {
        path = null;
        type = null;
        route = Lists.newLinkedList();
    }
	
	/**
	 * Create a new message content
	 * @param path the message path. 
	 * @param content the message content
	 */
    public Message(String path[], String type, T content) {
		this(path, type, content, Lists.<String>newLinkedList());
	}
    
    public Message(String path[], String type, T content, List<String> route) {
        this.path = new String[path.length];
        System.arraycopy(path, 0, this.path, 0, path.length);
        this.type = type;
        this.content = content;
        this.route = Lists.newLinkedList(route);
    }
    
    protected final void addClientKey(String clientId) {
        route.add(0, clientId);
    }
    
    protected final String getNextClientKey() {
        return route.size() > 0 ? route.remove(0) : null;
    }

    public List<String> getRoute() {
        return route;
    }

    /**
	 * Get the message path
	 * @return the message path
	 */
	public final String[] getPath() {
		return path;
	}

    public String getType() {
        return type;
    }

    /**
	 * Get the message
	 * @return the message
	 */
	public final T getPayload() {
		return content;
	}
    
    @Override
    public String toString() {
        return "{ route: " + routeToString(route) + ", path: " + Arrays.toString(path) + ", type: " + type + ", content: " + (content == null ? "null" : content.toString()) + "}";
    }

    public static <T> String routeToString(List<T> list) {
        return "[" + Joiner.on(",").join(list) + "]";
    }

    public interface Payload extends Serializable {}
}
