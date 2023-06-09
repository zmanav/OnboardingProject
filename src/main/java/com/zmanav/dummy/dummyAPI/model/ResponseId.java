package com.zmanav.dummy.dummyAPI.model;

public class ResponseId {

    private String requestId;
    private String requestMessage;

    public ResponseId(String requestId) {
        this.requestId = requestId;
        this.requestMessage = "Successfully submitted the request";
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public ResponseId(String requestId, String requestMessage) {
        this.requestId = requestId;
        this.requestMessage = requestMessage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
