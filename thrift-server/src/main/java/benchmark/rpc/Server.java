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
import java.sql.Timestamp;
import java.util.Arrays;

public class Server {
	public static void main(String[] args) {
		try {


			InetSocketAddress serverAddress = new InetSocketAddress("172.24.206.246", 8080);

			TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

			TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
			serverParams.protocolFactory(new TBinaryProtocol.Factory());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			serverParams.processor(new UserService.Processor<UserService.Iface>(new UserServiceServerImpl()));
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
