package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCMessage;
import com.ibm.darpc.DaRPCServerEndpoint;

import java.io.IOException;

public class DaRPCServerEvent<R extends DaRPCMessage, T extends DaRPCMessage> {
    private DaRPCServerEndpoint<R, T> endpoint;
    private R request;
    private T response;
    private int ticket;

    public DaRPCServerEvent(DaRPCServerEndpoint<R, T> endpoint, R request, T response) {
        this.endpoint = endpoint;
        this.request = request;
        this.response = response;
        this.ticket = 0;
    }

    public R getReceiveMessage() {
        return this.request;
    }

    public T getSendMessage() {
        return this.response;
    }

    public void triggerResponse() throws IOException {
    }

    public int getTicket() {
        return this.ticket;
    }

    void stamp(int ticket) {
        this.ticket = ticket;
    }
}
