package com.freeb.thrift.AccountClients;

import com.freeb.DaRPC.RdmaRpcRequest;
import com.freeb.DaRPC.RdmaRpcResponse;
import com.freeb.DaRPC.TRdmaClientRaw;
import com.freeb.thrift.SearchService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.Closeable;
import java.io.IOException;

public class ThriftSearchClientImpl implements Closeable {
	private Boolean rdma = true;
	// not thread safe
	public final TTransport transport;
	public final TProtocol protocol;
	public final SearchService.Client client;
	private static int clientNum = 0;
	public ThriftSearchClientImpl(String host, int port) {
		if(rdma){
			transport = new TRdmaClientRaw(null,new RdmaRpcRequest(),new RdmaRpcResponse());
		}else {
			transport = new TFramedTransport(new TSocket(host, port));
		}
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
