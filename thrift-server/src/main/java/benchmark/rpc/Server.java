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

			System.out.println("in thrift Server main() ");
			InetSocketAddress serverAddress = new InetSocketAddress("182.92.169.201", 8080);
			System.out.println("in thrift Server main() ==2==");
			TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);
			System.out.println("in thrift Server main() ==2.5==");
			TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
			System.out.println("in thrift Server main() ==3==");
			serverParams.protocolFactory(new TBinaryProtocol.Factory());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("in thrift Server main() ==4=="+timestamp.toString());
//			serverParams.processor(new UserService.Processor<UserService.Iface>(new UserServiceServerImpl()));
			System.out.println("in thrift Server main() ==5==");
			TServer server = new TThreadedSelectorServer(serverParams);
			timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("in thrift Server main() ==6=="+timestamp.toString());
			server.serve();
		}catch (TTransportException e){
			System.out.println("Server Exception is "+e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}

}
