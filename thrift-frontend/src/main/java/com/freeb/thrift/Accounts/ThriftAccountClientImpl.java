package com.freeb.thrift.Accounts;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.Closeable;
import java.io.IOException;

public class ThriftAccountClientImpl implements Closeable {
    // not thread safe
    public final TTransport transport;
    public final TProtocol protocol;
    public final AccountsService.Client client;
    private static int clientNum = 0;
    public ThriftAccountClientImpl(String host,int port) {
        // System.out.println("pre ThriftAccountClientImpl "+ clientNum);
        transport = new TFramedTransport(new TSocket(host, port));
        protocol = new TBinaryProtocol(transport);
        client = new AccountsService.Client(protocol);
        try {
            transport.open();
        } catch (TTransportException e) {
            System.out.println(e.getMessage());
            throw new Error(e);
        }
        // System.out.println("post ThriftAccountClientImpl "+ clientNum++);
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }
}
