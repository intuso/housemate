package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for an authentication response. An instance of this should always contain the broker instance id.
 * It will then contain either the problem, or both the connection id and user id
 *
 * @see com.intuso.housemate.api.object.root.Root#login(com.intuso.housemate.api.authentication.AuthenticationMethod)
 */
public class AuthenticationResponse implements Message.Payload {

    private String brokerInstanceId;
    private String connectionId;
    private String userId;
    private String problem;

    public AuthenticationResponse() {}

    /**
     * @param brokerInstanceId the instance id of the broker
     * @param connectionId the connection id
     * @param userId the id of the user that was logged in
     */
    public AuthenticationResponse(String brokerInstanceId, String connectionId, String userId) {
        this.brokerInstanceId = brokerInstanceId;
        this.connectionId = connectionId;
        this.userId = userId;
        this.problem = null;
    }

    /**
     * @param brokerInstanceId the instance id of the broker
     * @param problem the cause of the failed login attempt
     */
    public AuthenticationResponse(String brokerInstanceId, String problem) {
        this.brokerInstanceId = brokerInstanceId;
        this.connectionId = null;
        this.userId = null;
        this.problem = problem;
    }

    /**
     * Gets the instance id of the broker. Use this to check if the broker has been restarted
     * @return the instance id of the broker
     */
    public String getBrokerInstanceId() {
        return brokerInstanceId;
    }

    /**
     * Gets the connection id associated to this connection
     * @return the connection id associated to this connection
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * Gets the id of the user this connection is authenticated for
     * @return the id of the user this connection is authenticated for
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the reason that the login attempt failed
     * @return the reason that the login attempt failed
     */
    public String getProblem() {
        return problem;
    }

    @Override
    public String toString() {
        return (userId != null ? "connId=" + connectionId + " user=" + userId : "problem=" + problem);
    }
}
