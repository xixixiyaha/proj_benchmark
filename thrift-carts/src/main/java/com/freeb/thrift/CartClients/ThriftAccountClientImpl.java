package com.freeb.thrift.CartClients;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.Closeable;
import java.io.IOException;

public class ThriftAccountClientImpl implements Closeable {
    public final TTransport transport;
    public final TProtocol protocol;
    public final AccountService.Client client;
    private static int clientNum = 0;
    public ThriftAccountClientImpl(String host, int port) {

        transport = new TFramedTransport(new TSocket(host, port));
        protocol = new TBinaryProtocol(transport);
        client = new AccountService.Client(protocol);
        try {
            transport.open();
        } catch (TTransportException e) {
            System.out.println(e.getMessage());
            throw new Error(e);
        }
        // System.out.println("in ThriftSearchClientImpl/Accounts "+ clientNum++);
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }
}
