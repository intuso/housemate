package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 *
 * Message payload for an authentication response. An instance of this should always contain the server instance id.
 * It will then contain either the problem, or both the connection id and user id
 *
 * @see com.intuso.housemate.api.object.root.Root#login(com.intuso.housemate.api.authentication.AuthenticationMethod)
 */
public class AuthenticationResponse implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String serverInstanceId;
    private String connectionId;
    private String userId;
    private String problem;

    public AuthenticationResponse() {}

    /**
     * @param serverInstanceId the instance id of the server
     * @param connectionId the connection id
     * @param userId the id of the user that was logged in
     */
    public AuthenticationResponse(String serverInstanceId, String connectionId, String userId) {
        this(serverInstanceId, connectionId, userId, null);
    }

    /**
     * @param serverInstanceId the instance id of the server
     * @param problem the cause of the failed login attempt
     */
    public AuthenticationResponse(String serverInstanceId, String problem) {
        this(serverInstanceId, null, null, problem);
    }

    /**
     * @param serverInstanceId the instance id of the server
     * @param connectionId the connection id
     * @param userId the id of the user that was logged in
     */
    public AuthenticationResponse(String serverInstanceId, String connectionId, String userId, String problem) {
        this.serverInstanceId = serverInstanceId;
        this.connectionId = connectionId;
        this.userId = userId;
        this.problem = problem;
    }

    /**
     * Gets the instance id of the server. Use this to check if the server has been restarted
     * @return the instance id of the server
     */
    public String getServerInstanceId() {
        return serverInstanceId;
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
