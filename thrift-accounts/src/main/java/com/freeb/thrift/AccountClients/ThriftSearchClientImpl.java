package com.freeb.thrift.AccountClients;

import com.freeb.DaRPC.RawVersion.RdmaRpcRequest;
import com.freeb.DaRPC.RawVersion.RdmaRpcResponse;
import com.freeb.DaRPC.RawVersion.TRdmaClientRawTrans;
import com.freeb.thrift.SearchService;
import com.ibm.darpc.DaRPCClientEndpoint;
import com.ibm.darpc.DaRPCStream;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public class ThriftSearchClientImpl implements Closeable {

	private static final Logger logger = LoggerFactory.getLogger(ThriftSearchClientImpl.class.getName());

	// not thread safe
	public final TTransport transport_;
	public final TProtocol protocol_;
	public final SearchService.Client client_;
	private static int clientNum_ = 0;

	public ThriftSearchClientImpl(DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint, DaRPCStream<RdmaRpcRequest,RdmaRpcResponse> stream) {
		transport_ = new TRdmaClientRawTrans(endpoint,stream,new RdmaRpcRequest(),new RdmaRpcResponse());
		protocol_ = new TBinaryProtocol(transport_);
		client_ = new SearchService.Client(protocol_);
		logger.info("ThriftSearchClientImpl@ create Rdma Endpoint");
	}


	public ThriftSearchClientImpl(String host, int port) {

		transport_ = new TFramedTransport(new TSocket(host, port));

		protocol_ = new TBinaryProtocol(transport_);
		client_ = new SearchService.Client(protocol_);
		try {
			transport_.open();
		} catch (TTransportException e) {
			System.out.println(e.getMessage());
			throw new Error(e);
		}
		logger.info("ThriftSearchClientImpl@ create Socket");
	}

	@Override
	public void close() throws IOException {
		transport_.close();
	}

}
