package com.intuso.housemate.api.comms.message;

import com.intuso.housemate.api.comms.Message;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/03/13
 * Time: 09:08
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationResponse implements Message.Payload {

    private String brokerInstanceId;
    private String connectionId;
    private String userId;
    private String problem;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String brokerInstanceId, String connectionId, String userId) {
        this.brokerInstanceId = brokerInstanceId;
        this.connectionId = connectionId;
        this.userId = userId;
        this.problem = null;
    }

    public AuthenticationResponse(String brokerInstanceId, String problem) {
        this.brokerInstanceId = brokerInstanceId;
        this.connectionId = null;
        this.userId = null;
        this.problem = problem;
    }

    public String getBrokerInstanceId() {
        return brokerInstanceId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getProblem() {
        return problem;
    }

    @Override
    public String toString() {
        return (userId != null ? "connId=" + connectionId + " user=" + userId : "problem=" + problem);
    }
}
