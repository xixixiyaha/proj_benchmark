package com.freeb.thrift.AccountClients;

import com.freeb.DaRPC.RdmaRpcRequest;
import com.freeb.DaRPC.RdmaRpcResponse;
import com.freeb.DaRPC.TRdmaClientRaw;
import com.freeb.thrift.SearchService;
import com.ibm.darpc.DaRPCClientEndpoint;
import com.ibm.darpc.DaRPCClientGroup;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ThriftSearchClientImpl implements Closeable {
	private Boolean rdma = true;
	// not thread safe
	public final TTransport transport;
	public final TProtocol protocol;
	public final SearchService.Client client;
	private static int clientNum = 0;

	public ThriftSearchClientImpl(String host, int port,DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint) throws Exception {
		System.out.println("ThriftSearchClientImpl@ create Endpoint 1");

		InetSocketAddress address = new InetSocketAddress(host, port);
		System.out.println("ThriftSearchClientImpl@ create Endpoint 2 - start connect");
		endpoint.connect(address,1000);
		System.out.println("ThriftSearchClientImpl@ create Endpoint 3 - end connect");
		transport = new TRdmaClientRaw(endpoint,new RdmaRpcRequest(),new RdmaRpcResponse());
		transport.open();
		System.out.println("ThriftSearchClientImpl@ create Endpoint 4");
		protocol = new TBinaryProtocol(transport);
		client = new SearchService.Client(protocol);
		System.out.println("ThriftSearchClientImpl@ create Endpoint 5");
	}


	public ThriftSearchClientImpl(String host, int port) {

		transport = new TFramedTransport(new TSocket(host, port));

		protocol = new TBinaryProtocol(transport);
		client = new SearchService.Client(protocol);
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
