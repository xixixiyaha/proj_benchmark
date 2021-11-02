package benchmark.rpc;


import benchmark.rpc.thrift.UserService;
import benchmark.rpc.thrift.UserServiceServerImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.InetSocketAddress;

public class Server {
	public static void main(String[] args) throws TTransportException {
		System.out.println("in thrift Server main() ");
//		InetSocketAddress serverAddress = new InetSocketAddress("benchmark-server", 8080);
//
//		TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);
//		TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
//		serverParams.protocolFactory(new TBinaryProtocol.Factory());
//		serverParams.processor(new UserService.Processor<UserService.Iface>(new UserServiceServerImpl()));
//		TServer server = new TThreadedSelectorServer(serverParams);
//		server.serve();
	}

}
