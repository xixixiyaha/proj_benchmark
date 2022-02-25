package com.freeb;


import com.freeb.DaRPC.*;
import com.freeb.Utils.IPUtil;
import com.freeb.thrift.SearchService;
import com.freeb.thrift.SearchServer.SearchServiceServerImpl;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;

public class SearchServer {
	private static final Logger logger = LoggerFactory.getLogger(SearchServer.class);

	private static Boolean rdma= true;
	public static void main(String[] args) {
		try {
			if(rdma){
//				TRdmaService service = new TRdmaService(new SearchService.Processor<SearchService.Iface>(new SearchServiceServerImpl()));
//				service.setProtocolFactory(new TBinaryProtocol.Factory());
//				RdmaRpcResponse resp = new RdmaRpcResponse();
//				RdmaRpcRequest req = new RdmaRpcRequest();
//				try {
//					File file = new File("./test.txt");
//					FileInputStream fis = new FileInputStream(file);
//					ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
//					byte[] b = new byte[1024];
//					int n;
//					while ((n = fis.read(b)) != -1) {
//						bos.write(b, 0, n);
//					}
//					fis.close();
//					bos.close();
//					req.setLimit(bos.size());
//					req.writeToParam(bos.toByteArray(),0,bos.size());
//					req.setPos(0);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				DaRPCServerEvent<RdmaRpcRequest, RdmaRpcResponse> event = new DaRPCServerEvent<>(null,req,resp);
//				service.process(event);

				TRdmaServerRaw server = new TRdmaServerRaw();
				server.launch(args);
				return;
			}
			String serverHost = IPUtil.getIPAddress();
			System.out.println("init Server socket IP addr = "+serverHost);
			InetSocketAddress serverAddress = new InetSocketAddress(serverHost, 8080);

			TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

			TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
			serverParams.protocolFactory(new TBinaryProtocol.Factory());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			serverParams.processor(new SearchService.Processor<SearchService.Iface>(new SearchServiceServerImpl()));
			TServer server = new TThreadedSelectorServer(serverParams);
			timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("in com.freeb.thrift SearchServer main() ==  =="+timestamp.toString());
			server.serve();
		}catch (TTransportException e){
			System.out.println("SearchServer Exception is "+e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
