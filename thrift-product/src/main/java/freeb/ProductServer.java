package freeb;


import freeb.thrift.SearchServer.ProductServiceServerImpl;
import freeb.thrift.ProductService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;

public class ProductServer {
	public static void main(String[] args) {
		try {


			InetSocketAddress serverAddress = new InetSocketAddress("10.0.16.14", 8080);

			TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

			TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
			serverParams.protocolFactory(new TBinaryProtocol.Factory());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			serverParams.processor(new ProductService.Processor<ProductService.Iface>(new ProductServiceServerImpl()));
			TServer server = new TThreadedSelectorServer(serverParams);
			timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("in com.freeb.thrift ProductService main() ==  =="+timestamp.toString());
			server.serve();
		}catch (TTransportException e){
			System.out.println("ProductServer Exception is "+e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}

}
