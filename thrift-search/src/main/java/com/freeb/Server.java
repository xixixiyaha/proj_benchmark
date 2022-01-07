package com.freeb;


import com.freeb.thrift.SearchService;
import com.freeb.thrift.SearchServiceServerImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;

public class Server {
	public static void main(String[] args) {
		try {


			InetSocketAddress serverAddress = new InetSocketAddress("10.0.16.14", 8080);

			TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

			TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
			serverParams.protocolFactory(new TBinaryProtocol.Factory());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			serverParams.processor(new SearchService.Processor<SearchService.Iface>(new SearchServiceServerImpl()));
			TServer server = new TThreadedSelectorServer(serverParams);
			timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("in thrift Server main() ==  =="+timestamp.toString());
			server.serve();
		}catch (TTransportException e){
			System.out.println("Server Exception is "+e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}

}
